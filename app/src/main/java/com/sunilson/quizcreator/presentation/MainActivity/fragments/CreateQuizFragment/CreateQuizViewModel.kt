package com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Single
import javax.inject.Inject

@FragmentScope
class CreateQuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {

    val singleOrMultiple: ObservableBoolean = ObservableBoolean(false)
    val shuffleAnswers: ObservableBoolean = ObservableBoolean(false)
    val maxQuestionAmount: ObservableInt = ObservableInt(10)

    fun generateQuiz(): Single<Quiz> {
        loading.set(true)
        return repository.generateQuiz(selectedCategory?.id, shuffleAnswers.get(), singleOrMultiple.get(), maxQuestionAmount.get()).doFinally {
            loading.set(false)
        }.doOnError {
            errorMessage.set(it.message)
        }
    }
}