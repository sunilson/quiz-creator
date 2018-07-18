package com.sunilson.quizcreator.presentation.views.QuizView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.databinding.QuizQuestionCardViewBinding

class QuestionCardView : LinearLayout {

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
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(question != null && answerClicked != null) {
            binding.viewModel = QuestionCardViewModel(question!!, answerClicked!!)
        }
    }
}