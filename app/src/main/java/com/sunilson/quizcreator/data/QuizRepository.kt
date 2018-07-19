package com.sunilson.quizcreator.data

import android.app.Application
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface IQuizRepository {
    fun generateQuiz(categoryId: String? = null): Single<Quiz>
    fun addQuestion(question: Question): Completable
    fun addCategory(category: Category): Completable
    fun loadCategories(): Flowable<List<Category>>
    fun loadQuestions(categoryId: String? = null): Flowable<List<Question>>
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

    override fun generateQuiz(categoryId: String?): Single<Quiz> {
        return Single.just(Quiz(
                questions = mutableListOf(
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        )),
                        Question(text = "Frage 1", correctAnswerId = "bla", answers = mutableListOf(
                                Answer(text = "Antwort"),
                                Answer(id= "bla", text = "Antwort"),
                                Answer(text = "Antwort"),
                                Answer(text = "Antwort")
                        ))
                )
        ))
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
            database.quizDAO().getAllQuestions()
        } else {
            Flowable.just(listOf())
        }
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

        return null
    }
}