package com.sunilson.quizcreator.presentation.views.QuizView

import android.content.Context
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Quiz
import kotlinx.android.synthetic.main.quiz_view.view.*
import java.util.*


class QuizView(context: Context, val attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var startTime: Long = 0
    var quiz: Quiz? = null
        set(value) {
            field = value
            startTime = Date().time
            question_number.text = context.getString(R.string.question_number, 1)
            value?.let {
                stack_view.addListOfCards(it.questions.mapIndexed { index, value ->
                    QuestionCardView(context, value) {
                        Handler().postDelayed({
                            stack_view.removeCard()
                            question_number.text = context.getString(R.string.question_number, index + 2)
                        }, 1000)
                    }
                })
            }

            val handler = Handler()
            val runnable = object : Runnable {
                override fun run() {
                    timer.text = (Date().time - startTime).toString()
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(runnable)
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.quiz_view, this, true)
    }
}