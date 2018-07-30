package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.animation.LayoutTransition
import android.content.Context
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.databinding.QuizQestionCardViewMultipleBinding
import com.sunilson.quizcreator.presentation.views.AnswerView.AnswerView
import kotlinx.android.synthetic.main.quiz_qestion_card_view_multiple.view.*


class MultipleChoiceQuestionCardView(context: Context, question: Question, questionSubmitted: (Boolean) -> Unit) : QuestionCardView(context, question, questionSubmitted) {

    val binding: QuizQestionCardViewMultipleBinding = QuizQestionCardViewMultipleBinding.inflate(inflater, this, true)

    init {
        this.layoutTransition = LayoutTransition()
        confirm_question_button.setOnClickListener {
            for (i in 0 until question_answers.childCount) {
                val answerView = question_answers.getChildAt(i) as AnswerView
                answerView.showAnswerResult()
            }

            val containsIncorrectAnswers = question.answers.any { (!it.correctAnswer && it.chosen) || (it.correctAnswer && !it.chosen) }
            if (containsIncorrectAnswers) this.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.error_card_animation))
            questionSubmitted(!containsIncorrectAnswers)
        }
    }

    override fun initializeViewModel() {
        binding.viewModel = QuestionCardViewModel(question) {
            it.chosen = !it.chosen
        }
    }
}