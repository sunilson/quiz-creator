package com.sunilson.quizcreator.presentation.views.QuizView

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Quiz
import kotlinx.android.synthetic.main.quiz_view.view.*

class QuizView(context: Context, val attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var quiz: Quiz? = null
        set(value) {
            field = value
            value?.let {
                stack_view.addListOfCards(it.questions.map {
                    QuestionCardView(context, it) {
                        Handler().postDelayed({
                            stack_view.removeCard()
                        }, 1000)
                    }
                })
            }
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.quiz_view, this, true)
    }
}

