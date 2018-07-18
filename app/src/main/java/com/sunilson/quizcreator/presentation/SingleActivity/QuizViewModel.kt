package com.sunilson.quizcreator.presentation.SingleActivity

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.Application.di.ActivityScope
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class QuizViewModel @Inject constructor(val application: Application, val repository: IQuizRepository) : BaseViewModel() {

    init {
        //currentQuiz.addOnPropertyChangedCallback(object : )
    }

    /******** FETCHING *******/

    val currentQuiz: ObservableField<Quiz?> = ObservableField()
    val categories: ObservableList<Category> = ObservableArrayList<Category>()
    val questions: ObservableList<Question> = ObservableArrayList<Question>()

    fun loadCategories() {
        disposable.add(repository.loadCategories().subscribe {
            categories.clear()
            categories.addAll(it)
        })
    }

    fun loadQuestions() {
        disposable.add(repository.loadQuestions().subscribe {
            questions.clear()
            questions.addAll(it)
        })
    }

    fun queryQuestions(): Flowable<List<Question>> {
        return Flowable.just(null)
    }

    /******** CREATION *******/

    var creationQuestion: Question = Question()

    fun createQuestion(): Completable {
        errorMessage.set("")
        return repository.addQuestion(creationQuestion).doOnError { errorMessage.set(it.message) }
    }

    fun generateQuiz() {
        loading.set(true)
        disposable.add(repository.generateQuiz(selectedCategory?.id).doFinally { loading.set(false) }.subscribe(
                { currentQuiz.set(it) },
                { errorMessage.set(it.message) }))
    }

    fun addCategory(name: String): Completable {
        if (name.isEmpty()) return Completable.error(IllegalArgumentException(application.getString(R.string.category_name_empty)))
        return repository.addCategory(Category(name = name))
    }

    /******** UTILITY *******/

    var selectedCategory: Category? = null
    val disposable: CompositeDisposable = CompositeDisposable()

    fun selectCategory(category: Category) {
        selectedCategory = category
        creationQuestion.categoryId = category.id
    }

    fun unselectCategory(value: Boolean) {
        if (!value) selectedCategory = null
    }

    fun startQuestionCreation() {
        creationQuestion = Question()
    }

    fun reset() {
        errorMessage.set("")
        loading.set(false)
        working.set(false)
        currentQuiz.set(null)
        selectedCategory = null
        creationQuestion = Question()
    }

    fun onDestroy() {
        disposable.dispose()
    }
}