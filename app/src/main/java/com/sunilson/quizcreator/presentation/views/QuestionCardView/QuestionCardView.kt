package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.databinding.QuizQuestionCardViewBinding
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx

class QuestionCardView : ConstraintLayout {

    var answerClicked: ((Answer) -> Unit)? = null
    var question: Question? = null
    val binding: QuizQuestionCardViewBinding

    constructor(context: Context, question: Question, answerClicked: (Answer) -> Unit) : super(context) {
        this.answerClicked = answerClicked
        this.question = question
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = QuizQuestionCardViewBinding.inflate(inflater, this, true)
        setPadding(10.convertToPx(context), 15.convertToPx(context), 2.convertToPx(context), 10.convertToPx(context))
        background = ContextCompat.getDrawable(context, R.drawable.stacked_card_view_background)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(question != null && answerClicked != null) {
            binding.viewModel = QuestionCardViewModel(question!!, answerClicked!!)
        }
    }
}