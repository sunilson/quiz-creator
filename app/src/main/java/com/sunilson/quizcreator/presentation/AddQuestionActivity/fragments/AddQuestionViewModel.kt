package com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments

import android.app.Application
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.util.Log
import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import com.sunilson.quizcreator.presentation.shared.LocalSettingsManager
import io.reactivex.Completable
import javax.inject.Inject

@FragmentScope
class AddQuestionViewModel @Inject constructor(val application: Application, val localSettingsManager: LocalSettingsManager, repository: IQuizRepository) : BaseViewModel(repository) {

    var oldCategory: Category? = null
    var editMode: ObservableBoolean = ObservableBoolean(false)
    var observableQuestion: ObservableField<Question?> = ObservableField()
    var question
        get() = observableQuestion.get()
        set(value) {
            observableQuestion.set(value)
        }

    init {
        categories.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Category>>() {
            override fun onChanged(sender: ObservableList<Category>?) {}
            override fun onItemRangeRemoved(sender: ObservableList<Category>?, positionStart: Int, itemCount: Int) {}
            override fun onItemRangeMoved(sender: ObservableList<Category>?, fromPosition: Int, toPosition: Int, itemCount: Int) {}
            override fun onItemRangeInserted(sender: ObservableList<Category>?, positionStart: Int, itemCount: Int) {
                oldCategory = categories.find { it.id == localSettingsManager.getLastUsedCategory() }
                if (oldCategory != null) question?.category = oldCategory!!
            }

            override fun onItemRangeChanged(sender: ObservableList<Category>?, positionStart: Int, itemCount: Int) {}
        })
        loadCategories()


        observableQuestion.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                question?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        localSettingsManager.setLastUsedCategory(question!!.category.id)
                    }
                })
            }
        })
    }

    fun answersChanged(count: Int) {
        if (count > question!!.maxAnswers) question!!.maxAnswers++
    }

    fun createQuestion(): Completable {
        errorMessage.set("")
        return repository.addQuestion(question!!).doOnError { errorMessage.set(it.message) }
    }

    fun addCategory(name: String): Completable {
        if (name.isEmpty()) return Completable.error(IllegalArgumentException(application.getString(R.string.category_name_empty)))
        return repository.addCategory(Category(name = name))
    }

    fun loadQuestion(id: String) {
        editMode.set(true)
        disposable.add(repository.loadQuestionOnce(id).subscribe({
            question = it
        }, {
            Log.d("Linus", it.message)
        }))
    }

    fun startQuestionCreation(questionType: QuestionType) {
        question = Question(
                type = questionType,
                answers = mutableListOf(
                        Answer(text = application.getString(R.string.answer_default_text), correctAnswer = true)
                )
        )
    }
}