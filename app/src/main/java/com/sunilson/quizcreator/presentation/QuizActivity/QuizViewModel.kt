package com.sunilson.quizcreator.presentation.QuizActivity

import android.databinding.ObservableField
import com.sunilson.quizcreator.Application.di.ActivityScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class QuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {
    var currentQuiz: ObservableField<Quiz?> = ObservableField()

    fun loadQuiz(id: String): Single<Quiz> {
        return repository.loadQuiz(id).doAfterSuccess {
            currentQuiz.set(it)
        }
    }

    fun storeQuiz(): Completable {
        return repository.storeQuiz(currentQuiz.get()!!)
    }
}