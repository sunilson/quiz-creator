package com.sunilson.quizcreator.presentation.views.QuizView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import kotlinx.android.synthetic.main.quiz_question_answer.view.*

class AnswerView : FrameLayout {

    var answer: Answer? = null
        set(value) {
            field = value
            answer_text?.text = value?.text
        }

    constructor(context: Context, answer: Answer) : super(context) {
        this.answer = answer
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.quiz_question_answer, this, true)
    }

    fun showAnswerResult(correct: Boolean) {
        if (correct) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.correct))
        } else {
            setBackgroundColor(ContextCompat.getColor(context, R.color.wrong))
        }
        this.setOnClickListener(null)
    }
}