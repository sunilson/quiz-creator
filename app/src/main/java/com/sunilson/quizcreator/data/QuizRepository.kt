package com.sunilson.quizcreator.data

import android.app.Application
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.Exceptions.NoQuestionsFoundException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface IQuizRepository {
    fun generateQuiz(categoryId: String? = null, shuffleAnswers: Boolean = false, questionAmount: Int = 10): Single<Quiz>
    fun addQuestion(question: Question): Completable
    fun deleteQuestion(questionId: String): Completable
    fun updateQuestion(question: Question): Completable
    fun addCategory(category: Category): Completable
    fun loadCategories(): Flowable<List<Category>>
    fun loadQuiz(id: String): Single<Quiz>
    fun loadQuestions(categoryId: String? = null): Flowable<List<Question>>
    fun loadQuestionsOnce(categoryId: String? = null): Single<List<Question>>
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

    override fun generateQuiz(categoryId: String?, shuffleAnswers: Boolean, questionAmount: Int): Single<Quiz> {

        return if (categoryId != null) {
            database.quizDAO().getQuestionsForCategoriesOnce(arrayOf(categoryId)).map { questions ->
                if (questions.isEmpty()) throw NoQuestionsFoundException(application.getString(R.string.no_questions_found))
                val quiz = processQuizQuestions(questions, questionAmount)
                database.quizDAO().addQuiz(quiz)
                return@map quiz
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        } else {
            database.quizDAO().getAllQuestionsOnce().map { questions ->
                if (questions.isEmpty()) throw NoQuestionsFoundException(application.getString(R.string.no_questions_found))
                val quiz = processQuizQuestions(questions, questionAmount)
                database.quizDAO().addQuiz(quiz)
                return@map quiz
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun processQuizQuestions(questions: List<Question>, questionAmount: Int): Quiz {
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