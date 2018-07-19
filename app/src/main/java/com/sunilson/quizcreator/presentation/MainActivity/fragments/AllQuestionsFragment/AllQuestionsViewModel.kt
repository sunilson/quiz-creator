package com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment

import android.app.Application
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import javax.inject.Inject

@FragmentScope
class AllQuestionsViewModel @Inject constructor(val application: Application, repository: IQuizRepository) : BaseViewModel(repository) {

    val questions: ObservableList<Question> = ObservableArrayList<Question>()

    fun loadQuestions() {
        disposable.add(repository.loadQuestions().subscribe {
            questions.clear()
            questions.addAll(it)
        })
    }
}