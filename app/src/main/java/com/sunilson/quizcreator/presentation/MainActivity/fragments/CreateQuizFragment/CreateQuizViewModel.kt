package com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment

import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Single
import javax.inject.Inject

@FragmentScope
class CreateQuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {

    fun generateQuiz() : Single<Quiz> {
        loading.set(true)
        return repository.generateQuiz(selectedCategory?.id).doFinally {
            loading.set(false)
        }.doOnError {
            errorMessage.set(it.message)
        }
    }

}