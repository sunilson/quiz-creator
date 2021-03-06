package com.sunilson.quizcreator.data

import android.app.Application
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.data.models.Statistics
import com.sunilson.quizcreator.presentation.shared.ANSWERS_PER_QUIZ_QUESTION
import com.sunilson.quizcreator.presentation.shared.exceptions.NoQuestionsFoundException
import com.sunilson.quizcreator.presentation.shared.GOOD_THRESHOLD
import com.sunilson.quizcreator.presentation.shared.extensions.popRandomElement
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface IQuizRepository {
    fun generateQuiz(
        categoryId: String? = null,
        shuffleAnswers: Boolean = false,
        onlySingleChoice: Boolean = false,
        questionAmount: Int = 10
    ): Single<Quiz>

    fun storeQuiz(quiz: Quiz): Completable
    fun addQuestion(question: Question): Completable
    fun deleteQuestion(questionId: String): Completable
    fun updateQuestion(question: Question): Completable
    fun addCategory(category: Category): Completable
    fun updateCategory(category: Category): Completable
    fun deleteCategory(categoryId: String): Completable
    fun loadQuestionOnce(id: String): Single<Question>
    fun loadCategories(): Flowable<List<Category>>
    fun loadCategoriesOnce(): Single<List<Category>>
    fun loadQuiz(id: String): Single<Quiz>
    fun loadFinishedQuizzes(): Flowable<List<Quiz>>
    fun loadQuestions(categoryId: String? = null, query: String? = null): Flowable<List<Question>>
    fun loadQuestionsOnce(categoryId: String? = null, query: String? = null): Single<List<Question>>
    fun getStatistics(): Flowable<Statistics>
    fun resetStatistics(): Completable
    fun exportQuestionsAndCategories(uri: Uri): Completable
    fun importQuestionsAndCategories(path: Uri): Completable
}

@Singleton
class QuizRepository @Inject constructor(
    private val application: Application,
    private val database: QuizDatabase
) : IQuizRepository {

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

    override fun generateQuiz(
        categoryId: String?,
        shuffleAnswers: Boolean,
        onlySingleChoice: Boolean,
        questionAmount: Int
    ): Single<Quiz> {
        return Single.create<Quiz> {
            val allQuestions = database.quizDAO().getAllQuestionsOnce().blockingGet()
            val quizQuestions = if (onlySingleChoice && categoryId != null) {
                database.quizDAO()
                    .getAllQuestionsOnce(QuestionType.SINGLE_CHOICE, arrayOf(categoryId))
                    .blockingGet()
            } else if (onlySingleChoice) {
                database.quizDAO().getAllQuestionsOnce(QuestionType.SINGLE_CHOICE).blockingGet()
            } else if (categoryId != null) {
                database.quizDAO().getAllQuestionsOnce(arrayOf(categoryId)).blockingGet()
            } else {
                allQuestions
            }
            if (quizQuestions.isEmpty()) it.onError(
                NoQuestionsFoundException(
                    application.getString(
                        R.string.no_questions_found
                    )
                )
            )

            val quiz =
                processQuizQuestions(quizQuestions, allQuestions, questionAmount, shuffleAnswers)
            database.quizDAO().addQuiz(quiz)
            it.onSuccess(quiz)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    private fun sortQuestionsByCategory(questions: List<Question>): Map<String, List<Question>> {
        val result = mutableMapOf<String, MutableList<Question>>()

        questions.forEach {
            if (result[it.categoryId] == null) result[it.categoryId] = mutableListOf()
            result[it.categoryId]?.add(it)
        }

        return result
    }

    private fun processQuizQuestions(
        questions: List<Question>,
        allQuestions: List<Question>,
        questionAmount: Int,
        shuffleAnswers: Boolean
    ): Quiz {
        var quizQuestions = questions.shuffled().take(questionAmount)
        val random = Random()

        //Generate answer pool
        var answerPool = mutableMapOf<String, MutableList<Answer>>()
        allQuestions.forEach { question ->
            if (answerPool[question.categoryId] == null) answerPool[question.categoryId] =
                mutableListOf()
            question.answers.forEach {
                val copy = it.copy()
                copy.correctAnswer = false
                answerPool[question.categoryId]?.add(copy)
            }
        }

        //Shuffle Answer pool
        answerPool.forEach { _, mutableList ->
            mutableList.shuffle()
        }

        val answerPoolSave = answerPool.toMutableMap()

        //Fill quiz questions
        quizQuestions = quizQuestions.map {
            if (it.maxAnswers != it.answers.size) {
                var noQuestionFoundCount = 0
                while (it.answers.size < it.maxAnswers || it.answers.size < ANSWERS_PER_QUIZ_QUESTION) {
                    var answer: Answer? = null
                    var usedCategory: String? = null

                    if (it.onlySameCategory) {
                        //Find answer in answerpool category, if none found use random Answer
                        answer =
                            answerPool[it.categoryId]!!.find { a -> !it.answers.any { answer2 -> answer2.id == a.id } }
                    } else {
                        //Use list instead of map to randomize category order
                        val categoryList = answerPool.toList().toMutableList()
                        categoryList.shuffle()
                        //Search in all categories in the answer pool, but randomized
                        for (entry in categoryList) {
                            answer =
                                entry.second.find { a -> !it.answers.any { a2 -> a2.id == a.id } }
                            if (answer != null) {
                                //Set category where answer has been found, so we can remove it later
                                usedCategory = entry.first
                                break
                            }
                        }
                    }

                    //If no answer has been found, try refilling the answer pool 2 times, if still not found, use placeholder answer
                    if (answer == null) {
                        if (noQuestionFoundCount < 2) {
                            //Reset pool
                            answerPool = answerPoolSave.toMutableMap()
                            noQuestionFoundCount++
                        } else {
                            //Add placeholder answer, last resort
                            answer =
                                Answer(text = application.getString(R.string.not_enough_answers))
                            it.answers.add(answer)
                        }
                    } else {
                        //Answer has been found, add it to the question and remove it from the pool
                        it.answers.add(answer)
                        answerPool[usedCategory ?: it.categoryId]!!.remove(answer)
                    }
                }
            }
            it
        }

        //Shuffle Answers
        if (shuffleAnswers) {
            val shufflePool = mutableListOf<Answer>()
            quizQuestions.forEach { question ->
                question.answers.forEach {
                    if (!it.correctAnswer) shufflePool.add(it)
                }
            }

            quizQuestions = quizQuestions.map { question ->
                question.answers = question.answers.map { answer ->
                    if (answer.correctAnswer) answer
                    else {
                        shufflePool.popRandomElement(answer.id, 1f)
                    }
                }.toMutableList()
                question
            }.toMutableList()
        }

        quizQuestions = quizQuestions.map { question ->
            val tempAnswers = question.answers.shuffled().take(4).toMutableList()
            if (question.type == QuestionType.SINGLE_CHOICE && !tempAnswers.any { it.correctAnswer }) {
                tempAnswers[random.nextInt(4)] = question.answers.first { it.correctAnswer }
            }
            question.answers = tempAnswers
            question
        }.toMutableList()

        val quiz = Quiz(questions = quizQuestions)
        database.quizDAO().addQuiz(quiz)
        return quiz
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
        var singleChoiceAnswered = 0
        var multipleChoiceAnswered = 0
        var singleChoiceCorrect = 0
        var multipleChoiceCorrect = 0
        val correctPerCategoryAmount = mutableMapOf<String, Int>()
        val allPerCategoryAmount = mutableMapOf<String, Int>()
        val categoryRateResult = mutableMapOf<String, Float>()
        val categoryDates = mutableMapOf<String, Long>()
        var wholeDuration: Long = 0

        finishedQuizes.forEach { q ->
            wholeDuration += q.duration
            q.questions.forEach { question ->
                if (correctPerCategoryAmount[question.categoryId] == null) correctPerCategoryAmount[question.categoryId] =
                    0
                if (allPerCategoryAmount[question.categoryId] == null) allPerCategoryAmount[question.categoryId] =
                    0
                if (categoryDates[question.categoryId] == null) categoryDates[question.categoryId] =
                    q.timestamp

                if (question.type == QuestionType.SINGLE_CHOICE) singleChoiceAnswered++
                else multipleChoiceAnswered++

                val currentVal = allPerCategoryAmount[question.categoryId]!!
                allPerCategoryAmount[question.categoryId] = currentVal + 1

                if (question.correctlyAnswered) {
                    val currentCorrectVal = correctPerCategoryAmount[question.categoryId]!!
                    correctPerCategoryAmount[question.categoryId] = currentCorrectVal + 1

                    if (question.type == QuestionType.SINGLE_CHOICE) singleChoiceCorrect++
                    else multipleChoiceCorrect++
                }

                if (categoryDates[question.categoryId]!! < q.timestamp) categoryDates[question.categoryId] =
                    q.timestamp
            }
        }

        correctPerCategoryAmount.forEach { s, i ->
            categoryRateResult[s] = (i * 100f) / allPerCategoryAmount[s]!!
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
        if ((singleChoiceAnswered + multipleChoiceAnswered) != 0) result.averageCorrectRate =
            ((singleChoiceCorrect + multipleChoiceCorrect) * 100f) / (singleChoiceAnswered + multipleChoiceAnswered)
        result.averageCorrectRatePerCategory = categoryRateResult
        if (finishedQuizes.isNotEmpty()) result.averageDuration =
            wholeDuration / finishedQuizes.size
        result.finishedQuizAmountSevenDays = finishedQuizLastSevenDays
        result.finishedGoodQuizAmountSevenDays = finishedGoodQuizLastSevenDays
        result.lastAbsolvedDatePerCategory = categoryDates
        if (singleChoiceAnswered != 0) result.singleChoiceCorrectRate =
            (singleChoiceCorrect * 100) / singleChoiceAnswered
        if (multipleChoiceAnswered != 0) result.multipleChoiceCorrectRate =
            (multipleChoiceCorrect * 100) / multipleChoiceAnswered

        return result
    }

    override fun loadQuestionOnce(id: String): Single<Question> {
        return database.quizDAO().getQuestionOnce(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
            database.quizDAO().getAllQuestions(arrayOf(categoryId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            database.quizDAO().getAllQuestions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
            database.quizDAO().getAllQuestionsOnce(arrayOf(categoryId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            database.quizDAO().getAllQuestionsOnce().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        return database.quizDAO().getQuizOnce(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addCategory(category: Category): Completable {
        return database.quizDAO().getAllCategoriesOnce().flatMapCompletable { categories ->
            if (categories.any { it.name == category.name }) Completable.error(
                IllegalArgumentException(application.getString(R.string.duplicate_category_error))
            )
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

            allCategories.forEach {
                newMap[it.id] = 0f
            }

            it.averageCorrectRatePerCategory.forEach { s, fl ->
                val category = allCategories.firstOrNull { it.id == s }
                if (category != null) newMap[category.id] = fl
            }

            it.averageCorrectRatePerCategory = newMap
            it
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadFinishedQuizzes(): Flowable<List<Quiz>> {
        return database.quizDAO().getAllFinishedQuiz().map { list ->
            list.sortedBy { it.timestamp }.reversed()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun exportQuestionsAndCategories(uri: Uri): Completable {
        return Single
            .zip(
                database.quizDAO().getAllQuestionsOnce(),
                database.quizDAO().getAllCategoriesOnce(),
                BiFunction { t1: List<Question>, t2: List<Category> ->
                    val gson = Gson()
                    val jsonObject = JsonObject()
                    jsonObject.add("questions", gson.toJsonTree(t1))
                    jsonObject.add("categories", gson.toJsonTree(t2))
                    application.contentResolver.openOutputStream(uri)?.use {
                        val writer = OutputStreamWriter(it)
                        writer.use { writer ->
                            writer.write("")
                            writer.write(jsonObject.toString())
                            writer.flush()
                        }
                    }
                    Unit
                })
            .ignoreElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun importQuestionsAndCategories(path: Uri): Completable {
        return Completable.create {
            val gson = Gson()
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
            val questions =
                gson.fromJson<List<Question>>(jsonObject["questions"], questionListType)
            val categories =
                gson.fromJson<List<Category>>(jsonObject["categories"], categoryListType)
            database.quizDAO().addQuestions(*questions.toTypedArray())
            database.quizDAO().addCategories(*categories.toTypedArray())
            it.onComplete()
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