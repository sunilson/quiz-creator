package com.sunilson.quizcreator.presentation.addQuestionActivity.fragments

import android.app.Application
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.sunilson.quizcreator.R

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.shared.LocalSettingsManager
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel
import io.reactivex.Completable



class AddQuestionViewModel (
    val application: Application,
    val localSettingsManager: LocalSettingsManager,
    repository: IQuizRepository
) : BaseViewModel(repository) {

    var oldCategory: Category? = null
    var editMode: ObservableBoolean = ObservableBoolean(false)
    var observableQuestion: ObservableField<Question?> = ObservableField()
    var question
        get() = observableQuestion.get()
        set(value) {
            observableQuestion.set(value)
        }

    init {
        categories.addOnListChangedCallback(object :
            ObservableList.OnListChangedCallback<ObservableList<Category>>() {
            override fun onChanged(sender: ObservableList<Category>?) {}
            override fun onItemRangeRemoved(
                sender: ObservableList<Category>?,
                positionStart: Int,
                itemCount: Int
            ) {
            }

            override fun onItemRangeMoved(
                sender: ObservableList<Category>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
            }

            override fun onItemRangeInserted(
                sender: ObservableList<Category>?,
                positionStart: Int,
                itemCount: Int
            ) {
                oldCategory = categories.find { it.id == localSettingsManager.lastUsedCategory }
                if (oldCategory != null) question?.category = oldCategory!!
            }

            override fun onItemRangeChanged(
                sender: ObservableList<Category>?,
                positionStart: Int,
                itemCount: Int
            ) {
            }
        })
        loadCategories()


        observableQuestion.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                question?.addOnPropertyChangedCallback(object :
                    Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        localSettingsManager.lastUsedCategory = question!!.category.id
                    }
                })
            }
        })
    }

    fun answersChanged(count: Int) {
        if (count > question!!.maxAnswers) question!!.maxAnswers++
    }

    fun createQuestion(): Completable {
        return repository.addQuestion(question!!)
    }

    fun addCategory(name: String): Completable {
        if (name.isEmpty()) return Completable.error(
            IllegalArgumentException(
                application.getString(
                    R.string.category_name_empty
                )
            )
        )
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
                Answer(
                    text = application.getString(R.string.answer_default_text),
                    correctAnswer = true
                )
            )
        )
    }
}