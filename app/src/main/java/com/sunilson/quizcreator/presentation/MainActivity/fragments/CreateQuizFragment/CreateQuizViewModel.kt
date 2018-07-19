package com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment

import android.databinding.ObservableField
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import javax.inject.Inject

@FragmentScope
class CreateQuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {

    val currentQuiz: ObservableField<Quiz?> = ObservableField()

    fun generateQuiz() {
        loading.set(true)
        disposable.add(repository.generateQuiz(selectedCategory?.id).doFinally { loading.set(false) }.subscribe(
                { currentQuiz.set(it) },
                { errorMessage.set(it.message) }))
    }

}