package com.sunilson.quizcreator.presentation.quizActivity

import androidx.databinding.ObservableField
import com.sunilson.quizcreator.application.di.scopes.ActivityScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel
import com.sunilson.quizcreator.presentation.shared.GOOD_THRESHOLD
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class QuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {
    var currentQuiz: ObservableField<Quiz?> = ObservableField()
    val finalDuration: Int
        get() {
            return if (currentQuiz.get() != null) (currentQuiz.get()!!.duration / 1000).toInt()
            else 0
        }

    val correctQuestions: Int
        get() {
            return if (currentQuiz.get() != null) currentQuiz.get()!!.correctAnswers
            else 0
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

    fun loadQuiz(id: String) {
        loading.set(true)
        disposable.add(repository.loadQuiz(id).subscribe({
            currentQuiz.set(it)
        }, {

        }))
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