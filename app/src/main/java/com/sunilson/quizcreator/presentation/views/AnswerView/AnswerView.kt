package com.sunilson.quizcreator.presentation.views.AnswerView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.databinding.QuizQuestionAnswerBinding

class AnswerView : ConstraintLayout {

    var answer: Answer? = null
        set(value) {
            field = value
            if (value != null) answerViewModel?.answer = value
        }

    var showDivider: Boolean = false
        set(value) {
            field = value
            answerViewModel?.showDivider = value
        }

    val binding: QuizQuestionAnswerBinding
    var answerViewModel: AnswerViewModel? = null

    constructor(context: Context, answer: Answer, showDivider: Boolean) : super(context) {
        this.answer = answer
        this.showDivider = showDivider
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = QuizQuestionAnswerBinding.inflate(inflater, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (answer != null && answerViewModel == null) {
            answerViewModel = AnswerViewModel(answer!!, showDivider)
            binding.viewModel = answerViewModel
        }
    }

    fun showAnswerResult(correct: Boolean) {
        answerViewModel?.correct = correct
        this.setOnClickListener(null)
    }
}