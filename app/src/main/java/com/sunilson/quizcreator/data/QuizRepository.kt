package com.sunilson.quizcreator.data

import android.app.Application
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.*
import com.sunilson.quizcreator.presentation.shared.Exceptions.NoQuestionsFoundException
import com.sunilson.quizcreator.presentation.shared.GOOD_THRESHOLD
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface IQuizRepository {
    fun generateQuiz(categoryId: String? = null, shuffleAnswers: Boolean = false, onlySingleChoice: Boolean = false, questionAmount: Int = 10): Single<Quiz>
    fun storeQuiz(quiz: Quiz): Completable
    fun addQuestion(question: Question): Completable
    fun deleteQuestion(questionId: String): Completable
    fun updateQuestion(question: Question): Completable
    fun addCategory(category: Category): Completable
    fun loadCategories(): Flowable<List<Category>>
    fun loadCategoriesOnce(): Single<List<Category>>
    fun loadQuiz(id: String): Single<Quiz>
    fun loadQuestions(categoryId: String? = null): Flowable<List<Question>>
    fun loadQuestionsOnce(categoryId: String? = null): Single<List<Question>>
    fun getStatistics(): Flowable<Statistics>
    fun resetStatistics(): Completable
}

@Singleton
class QuizRepository @Inject constructor(private val application: Application, private val database: QuizDatabase) : IQuizRepository {

    override fun addQuestion(question: Question): Completable {

        validateQuestion(question)?.let { return it }

        return Completable.create {
            database.quizDAO().addQuestion(question)
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteQuestion(questionId: String): Completable {
        return Completable.create {
            val affected = database.quizDAO().removeQuestion(questionId)
            if (affected == 0) it.onError(IllegalArgumentException(application.getString(R.string.question_not_found)))
            else it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateQuestion(question: Question): Completable {
        validateQuestion(question)?.let { return it }

        return Completable.create {
            database.quizDAO().updateQuestion(question)
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun generateQuiz(categoryId: String?, shuffleAnswers: Boolean, onlySingleChoice: Boolean, questionAmount: Int): Single<Quiz> {
        val single = if (onlySingleChoice && categoryId != null) {
            database.quizDAO().getAllQuestionsOnce(QuestionType.SINGLE_CHOICE, arrayOf(categoryId))
        } else if (onlySingleChoice) {
            database.quizDAO().getAllQuestionsOnce(QuestionType.SINGLE_CHOICE)
        } else if (categoryId != null) {
            database.quizDAO().getAllQuestionsOnce(arrayOf(categoryId))
        } else {
            database.quizDAO().getAllQuestionsOnce()
        }

        return single.map { questions ->
            if (questions.isEmpty()) throw NoQuestionsFoundException(application.getString(R.string.no_questions_found))
            val quiz = processQuizQuestions(questions, questionAmount, shuffleAnswers)
            database.quizDAO().addQuiz(quiz)
            return@map quiz
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun storeQuiz(quiz: Quiz): Completable {

        return if (quiz.finished) {
            database.quizDAO().getAllQuizOnce().flatMapCompletable { res ->

                val result = Statistics()
                val allQuizes = res.toMutableList()
                //Add new quiz to all quizes
                allQuizes[allQuizes.indexOfFirst { it.id == quiz.id }] = quiz

                val finishedQuizes = allQuizes.filter { it.finished }
                val quizCorrectRates = finishedQuizes.map { q ->
                    (q.correctAnswers * 100) / q.questions.size
                }

                val correctPerCategoryAmount = mutableMapOf<String, Int>()
                val wrongPerCategoryRate = mutableMapOf<String, Int>()
                val categoryRateResult = mutableMapOf<String, Float>()
                var wholeDuration: Long = 0

                finishedQuizes.forEach { q ->
                    wholeDuration += q.duration
                    q.questions.forEach { question ->
                        if (correctPerCategoryAmount[question.categoryId] == null) {
                            correctPerCategoryAmount[question.categoryId] = 0
                        }
                        if (wrongPerCategoryRate[question.categoryId] == null) {
                            wrongPerCategoryRate[question.categoryId] = 0
                        }

                        if (question.correctlyAnswered) {
                            val currentVal = correctPerCategoryAmount[question.categoryId]!!
                            correctPerCategoryAmount[question.categoryId] = currentVal + 1
                        } else {
                            val currentVal = wrongPerCategoryRate[question.categoryId]!!
                            wrongPerCategoryRate[question.categoryId] = currentVal + 1
                        }
                    }
                }

                correctPerCategoryAmount.forEach { s, i ->
                    categoryRateResult[s] = (i * 100f) / (correctPerCategoryAmount[s]!! + wrongPerCategoryRate[s]!!)
                }

                val finishedQuizLastSevenDays = mutableListOf(0, 0, 0, 0, 0, 0, 0)
                val finishedGoodQuizLastSevenDays = mutableListOf(0, 0, 0, 0, 0, 0, 0)

                finishedQuizes.filter {
                    DateTime(it.timestamp).isAfter(DateTime.now().minusDays(7)) && it.finished
                }.forEach { quiz ->
                    val index = DateTime.now().dayOfMonth - DateTime(quiz.timestamp).dayOfMonth
                    if (index < finishedQuizLastSevenDays.size) {
                        finishedQuizLastSevenDays[index]++
                        if (quiz.questions.filter { it.correctlyAnswered }.size * 100 / quiz.questions.size >= GOOD_THRESHOLD) {
                            finishedGoodQuizLastSevenDays[index]++
                        }
                    }
                }

                result.finishedQuizAmount = finishedQuizes.size
                result.unfinishedQuizAmount = allQuizes.size - finishedQuizes.size
                result.averageCorrectRate = quizCorrectRates.average().toFloat()
                result.averageCorrectRatePerCategory = categoryRateResult
                result.averageDuration = wholeDuration / finishedQuizes.size
                result.finishedQuizAmountSevenDays = finishedQuizLastSevenDays
                result.finishedGoodQuizAmountSevenDays = finishedGoodQuizLastSevenDays

                database.quizDAO().setStatistics(result)
                database.quizDAO().addQuiz(quiz)

                Completable.complete()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        } else {
            Completable.create {
                database.quizDAO().addQuiz(quiz)
                it.onComplete()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun processQuizQuestions(questions: List<Question>, questionAmount: Int, shuffleAnswers: Boolean): Quiz {

        if (shuffleAnswers) {
            //TODO Antworten von anderen Fragen der gleichen Kategorie holen
        }

        var quizQuestions = questions.map { question ->
            question.answers = question.answers.filterIndexed { index, answer ->
                index <= 3
            }.toMutableList()
            question
        }.toMutableList()

        quizQuestions.shuffle()
        quizQuestions = quizQuestions.take(questionAmount).toMutableList()

        val quiz = Quiz(
                questions = quizQuestions,
                timestamp = Date().time
        )

        database.quizDAO().addQuiz(quiz)
        return quiz
    }

    override fun loadCategories(): Flowable<List<Category>> {
        return database.quizDAO().getAllCategories().map {
            mutableListOf(
                    Category("general", application.getString(R.string.general))
            ).plus(it)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadCategoriesOnce(): Single<List<Category>> {
        return database.quizDAO().getAllCategoriesOnce().map {
            mutableListOf(
                    Category("general", application.getString(R.string.general))
            ).plus(it)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadQuestions(categoryId: String?): Flowable<List<Question>> {
        return if (categoryId == null) {
            database.quizDAO().getAllQuestions().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        } else {
            Flowable.just(listOf())
        }
    }

    override fun loadQuestionsOnce(categoryId: String?): Single<List<Question>> {
        return database.quizDAO().getAllQuestionsOnce().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadQuiz(id: String): Single<Quiz> {
        return database.quizDAO().getQuizOnce(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun addCategory(category: Category): Completable {
        return database.quizDAO().getAllCategoriesOnce().flatMapCompletable { categories ->
            if (categories.any { it.name == category.name }) Completable.error(IllegalArgumentException(application.getString(R.string.duplicate_category_error)))
            else {
                database.quizDAO().addCategory(category)
                Completable.complete()
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getStatistics(): Flowable<Statistics> {
        return database.quizDAO().getStatistics().map {
            val newMap = mutableMapOf<String, Float>()
            val allCategories = loadCategoriesOnce().blockingGet()

            it.averageCorrectRatePerCategory.forEach { s, fl ->
                newMap[allCategories.first { it.id == s }.name] = fl
            }

            it.averageCorrectRatePerCategory = newMap
            it
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun resetStatistics(): Completable {
        return Completable.create {
            database.quizDAO().setStatistics(Statistics())
            database.quizDAO().deleteAllQuiz()
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    private fun validateQuestion(question: Question): Completable? {
        if (question.text.isEmpty()) {
            return Completable.error(IllegalArgumentException(application.getString(R.string.question_empty_error)))
        }

        if (question.categoryId.isEmpty()) {
            return Completable.error(IllegalArgumentException(application.getString(R.string.question_category_not_set)))
        }

        question.answers.forEach {
            if (it.text.isEmpty()) {
                return Completable.error(IllegalArgumentException(application.getString(R.string.question_answers_empty)))
            }
        }

        if (!question.answers.any { it.correctAnswer }) {
            return Completable.error(IllegalArgumentException(application.getString(R.string.question_no_correct_answer_error)))
        }

        //TODO Check If Category exists!

        return null
    }
}