package com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.util.Log
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import javax.inject.Inject

@FragmentScope
class AllQuestionsViewModel @Inject constructor(val application: Application, repository: IQuizRepository) : BaseViewModel(repository) {

    init {
        loadCategories()
    }

    val questions: ObservableList<Question> = ObservableArrayList<Question>()

    fun loadQuestions() {
        loading.set(true)
        disposable.add(repository.loadQuestionsOnce().subscribe({
            if (categories.size > 0) {
                it.forEach { question ->
                    question.categoryName = categories.find { question.categoryId == it.id }?.name ?: ""
                }
            }
            questions.clear()
            questions.addAll(it)
            loading.set(false)
        }, {
            loading.set(false)
            Log.d("Linus", it.message)
        }))
    }

    fun deleteQuestion(question: Question): Completable {
        return repository.deleteQuestion(question.id)
    }

    fun updateQuestion(question: Question): Completable {
        return repository.updateQuestion(question)
    }
}