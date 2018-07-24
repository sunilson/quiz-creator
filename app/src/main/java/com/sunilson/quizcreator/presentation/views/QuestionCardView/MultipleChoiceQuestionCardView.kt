package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.content.Context
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.databinding.QuizQestionCardViewMultipleBinding
import com.sunilson.quizcreator.presentation.views.AnswerView.AnswerView
import kotlinx.android.synthetic.main.quiz_qestion_card_view_multiple.view.*

class MultipleChoiceQuestionCardView(context: Context, question: Question, questionSubmitted: (Boolean) -> Unit) : QuestionCardView(context, question, questionSubmitted) {

    val binding: QuizQestionCardViewMultipleBinding = QuizQestionCardViewMultipleBinding.inflate(inflater, this, true)
    val markedAnswers = mutableListOf<Answer>()

    init {
        confirm_question_button.setOnClickListener {
            //TODO Animation abspielen
            for (i in 0 until question_answers.childCount) {
                val answerView = question_answers.getChildAt(i) as AnswerView
                answerView.showAnswerResult()
            }

            val correctAmount = question.answers.count { it.correctAnswer } == markedAnswers.size
            val containsIncorrectAnswers = markedAnswers.any { !it.correctAnswer }
            val correct = correctAmount && !containsIncorrectAnswers
            if (!correct) this.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.error_card_animation))
            questionSubmitted(correct)
        }
    }

    override fun initializeViewModel() {
        binding.viewModel = QuestionCardViewModel(question) {
            if (markedAnswers.contains(it)) {
                markedAnswers.remove(it)
            } else {
                markedAnswers.add(it)
            }
        }
    }
}