package com.sunilson.quizcreator.presentation.views.answerView

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.databinding.QuizQuestionAnswerBinding
import com.sunilson.quizcreator.presentation.shared.ERROR_ANIMATION_DURATION

class AnswerView : ConstraintLayout {

    val answer: Answer?
        get() {
            return if (answerViewModel != null) answerViewModel.answer
            else null
        }

    private lateinit var binding: QuizQuestionAnswerBinding
    private lateinit var answerViewModel: AnswerViewModel

    constructor(context: Context, answer: Answer, showDivider: Boolean) : super(context) {
        initWithViewModel(answer, showDivider)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private fun initWithViewModel(answer: Answer, showDivider: Boolean) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = QuizQuestionAnswerBinding.inflate(inflater, this, true)
        answerViewModel = AnswerViewModel(answer, showDivider)
        binding.viewModel = answerViewModel
    }

    fun mark() {
        answerViewModel.marked = !answerViewModel.marked
    }

    fun unmark() {
        answerViewModel.marked = false
    }

    fun showAnswerResult() {
        if (answer != null) {
            if (answer!!.correctAnswer) {
                answerViewModel.correct = true
            } else {
                unmark()
                Handler().postDelayed({
                    answerViewModel.correct = false
                }, ERROR_ANIMATION_DURATION)
            }
            this.setOnClickListener(null)
        }
    }
}