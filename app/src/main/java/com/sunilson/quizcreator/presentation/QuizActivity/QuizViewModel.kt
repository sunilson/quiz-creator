package com.sunilson.quizcreator.presentation.QuizActivity

import android.databinding.ObservableField
import com.sunilson.quizcreator.Application.di.scopes.ActivityScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import com.sunilson.quizcreator.presentation.shared.GOOD_THRESHOLD
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class QuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {
    var currentQuiz: ObservableField<Quiz?> = ObservableField()
    val finalDuration: Int by lazy {
        (currentQuiz.get()!!.duration / 1000).toInt()
    }

    val correctQuestions: Int by lazy {
        currentQuiz.get()!!.correctAnswers
    }

    val wonQuiz: Boolean
        get() {
            val quiz = currentQuiz.get()
            return if (quiz != null) {
                (quiz.correctAnswers * 100) / quiz.questions.size >= GOOD_THRESHOLD
            } else {
                false
            }
        }

    fun generateQuiz(selectedCategory: String?, shuffleAnswers: Boolean, onlySingle: Boolean, maxQuestionAmount: Int): Single<Quiz> {
        loading.set(true)
        return repository.generateQuiz(selectedCategory, shuffleAnswers, onlySingle, maxQuestionAmount).doOnSuccess {
            currentQuiz.set(it)
        }
    }


    fun storeQuiz(): Completable {
        return repository.storeQuiz(currentQuiz.get()!!)
    }
}