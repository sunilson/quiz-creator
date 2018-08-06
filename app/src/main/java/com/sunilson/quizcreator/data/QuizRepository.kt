package com.sunilson.quizcreator.data

import android.app.Application
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.*
import com.sunilson.quizcreator.presentation.shared.Exceptions.NoQuestionsFoundException
import com.sunilson.quizcreator.presentation.shared.GOOD_THRESHOLD
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.popRandomElement
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.io.*
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
    fun updateCategory(category: Category): Completable
    fun deleteCategory(categoryId: String): Completable
    fun loadCategories(): Flowable<List<Category>>
    fun loadCategoriesOnce(): Single<List<Category>>
    fun loadQuiz(id: String): Single<Quiz>
    fun loadQuestions(categoryId: String? = null, query: String? = null): Flowable<List<Question>>
    fun loadQuestionsOnce(categoryId: String? = null, query: String? = null): Single<List<Question>>
    fun getStatistics(): Flowable<Statistics>
    fun resetStatistics(): Completable
    fun exportQuestionsAndCategories(): Completable
    fun importQuestionsAndCategories(path: Uri): Completable
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
                database.quizDAO().setStatistics(calculateStatistics(res, quiz))
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

    private fun calculateStatistics(res: List<Quiz>, quiz: Quiz): Statistics {
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
        val categoryDates = mutableMapOf<String, Long>()
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
                if (categoryDates[question.categoryId] == null) {
                    categoryDates[question.categoryId] = q.timestamp
                }

                if (question.correctlyAnswered) {
                    val currentVal = correctPerCategoryAmount[question.categoryId]!!
                    correctPerCategoryAmount[question.categoryId] = currentVal + 1
                } else {
                    val currentVal = wrongPerCategoryRate[question.categoryId]!!
                    wrongPerCategoryRate[question.categoryId] = currentVal + 1
                }

                if (categoryDates[question.categoryId]!! < q.timestamp) categoryDates[question.categoryId] = q.timestamp
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
        result.lastAbsolvedDatePerCategory = categoryDates

        return result
    }

    private fun processQuizQuestions(questions: List<Question>, questionAmount: Int, shuffleAnswers: Boolean): Quiz {

        var quizQuestions = questions.toMutableList()

        if (shuffleAnswers) {
            val answerPool = mutableMapOf<String, MutableList<Answer>>()
            quizQuestions.forEach { question ->
                if (answerPool[question.categoryId] == null) answerPool[question.categoryId] = mutableListOf()
                question.answers.forEach {
                    if (!it.correctAnswer) answerPool[question.categoryId]?.add(it)
                }
            }

            quizQuestions = quizQuestions.map { question ->
                question.answers = question.answers.map { answer ->
                    if (answer.correctAnswer) answer
                    else {
                        answerPool[question.categoryId]!!.popRandomElement(answer.id, 1f)
                    }
                }.toMutableList()
                question
            }.toMutableList()
        }

        quizQuestions = quizQuestions.map { question ->
            val tempAnswers = question.answers.shuffled().take(4).toMutableList()
            if (question.type == QuestionType.SINGLE_CHOICE && !tempAnswers.any { it.correctAnswer }) {
                tempAnswers[Random().nextInt(5)] = question.answers.first { it.correctAnswer }
            }
            question.answers = tempAnswers
            question
        }.toMutableList()

        quizQuestions.shuffle()
        quizQuestions = quizQuestions.take(questionAmount).toMutableList()

        val quiz = Quiz(questions = quizQuestions)
        database.quizDAO().addQuiz(quiz)
        return quiz
    }

    override fun loadCategories(): Flowable<List<Category>> {
        return database.quizDAO().getAllCategories().map {
            val completeList = mutableListOf(
                    Category("general", application.getString(R.string.general))
            ).plus(it)

            completeList.map {
                it.questionCount = database.quizDAO().countCategoryQuestions(it.id)
                it
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadCategoriesOnce(): Single<List<Category>> {
        return database.quizDAO().getAllCategoriesOnce().map {
            mutableListOf(
                    Category("general", application.getString(R.string.general))
            ).plus(it)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadQuestions(categoryId: String?, query: String?): Flowable<List<Question>> {
        val result = if (categoryId != null) {
            database.quizDAO().getAllQuestions(arrayOf(categoryId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        } else {
            database.quizDAO().getAllQuestions().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        return result.map { questions ->
            if (query == null) questions
            else {
                questions.filter {
                    it.text.contains(query, ignoreCase = true)
                }
            }
        }
    }

    override fun loadQuestionsOnce(categoryId: String?, query: String?): Single<List<Question>> {

        val result = if (categoryId != null) {
            database.quizDAO().getAllQuestionsOnce(arrayOf(categoryId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        } else {
            database.quizDAO().getAllQuestionsOnce().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }

        return result.map { questions ->
            if (query == null) questions
            else {
                questions.filter {
                    it.text.contains(query, ignoreCase = true)
                }
            }
        }
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

    override fun updateCategory(category: Category): Completable {
        return Completable.create {
            database.quizDAO().updateCategory(category)
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteCategory(categoryId: String): Completable {
        return Completable.create {
            database.quizDAO().removeQuestionsWithCategory(categoryId)
            database.quizDAO().removeCategory(categoryId)
            it.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun getStatistics(): Flowable<Statistics> {
        return database.quizDAO().getStatistics().map {
            val newMap = mutableMapOf<String, Float>()
            val allCategories = loadCategoriesOnce().blockingGet()

            it.averageCorrectRatePerCategory.forEach { s, fl ->
                newMap[allCategories.first { it.id == s }.id] = fl
            }

            it.averageCorrectRatePerCategory = newMap
            it
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun exportQuestionsAndCategories(): Completable {
        return Single.zip(database.quizDAO().getAllQuestionsOnce(), database.quizDAO().getAllCategoriesOnce(), BiFunction { t1: List<Question>, t2: List<Category> ->
            val gson = Gson()
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

            val jsonObject = JsonObject()
            jsonObject.add("questions", gson.toJsonTree(t1))
            jsonObject.add("categories", gson.toJsonTree(t2))

            val file = File(dir, "quiz_export.json")
            if (!file.exists()) file.createNewFile()

            val outputStream = FileOutputStream(file)
            val writer = OutputStreamWriter(outputStream)
            writer.write("")
            writer.write(jsonObject.toString())
            writer.close()
            outputStream.flush()
            outputStream.close()
            true
        }).flatMapCompletable {
            Completable.complete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun importQuestionsAndCategories(path: Uri): Completable {
        return Completable.create {
            val gson = Gson()
            //val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

            if (!path.path.endsWith(".json")) {
                it.onError(Exception(application.getString(R.string.invalid_file)))
            } else {
                val jsonReader = if (path.scheme == "content") {
                    val inputStream = application.contentResolver.openInputStream(path)
                    JsonReader(InputStreamReader(inputStream))
                } else {
                    val file = File(path.path)
                    JsonReader(FileReader(file))
                }

                val parser = JsonParser()
                val element = parser.parse(jsonReader)
                val jsonObject = element.asJsonObject
                val questionListType = object : TypeToken<List<Question>>() {}.type
                val categoryListType = object : TypeToken<List<Category>>() {}.type
                val questions = gson.fromJson<List<Question>>(jsonObject["questions"], questionListType)
                val categories = gson.fromJson<List<Category>>(jsonObject["categories"], categoryListType)
                database.quizDAO().addQuestions(*questions.toTypedArray())
                database.quizDAO().addCategories(*categories.toTypedArray())
                it.onComplete()
            }
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