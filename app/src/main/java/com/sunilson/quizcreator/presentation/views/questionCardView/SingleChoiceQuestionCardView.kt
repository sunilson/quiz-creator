package com.sunilson.quizcreator.presentation.views.questionCardView

import android.content.Context
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.databinding.QuizQuestionCardViewSingleBinding

class SingleChoiceQuestionCardView(context: Context, question: Question, questionSubmitted: (Boolean) -> Unit) : QuestionCardView(context, question, questionSubmitted) {

    val binding: QuizQuestionCardViewSingleBinding = QuizQuestionCardViewSingleBinding.inflate(inflater, this, true)

    override fun initializeViewModel() {
        binding.viewModel = QuestionCardViewModel(question) {
            it.chosen = true
            if (!it.correctAnswer) {
                this.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.error_card_animation))
            }
            questionSubmitted.invoke(it.correctAnswer)
        }
    }
}