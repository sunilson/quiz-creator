package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx

abstract class QuestionCardView(context: Context,
                                val question: Question,
                                val questionSubmitted: (Boolean) -> Unit) : LinearLayout(context) {

    protected val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
        setPadding(10.convertToPx(context), 15.convertToPx(context), 2.convertToPx(context), 10.convertToPx(context))
        background = ContextCompat.getDrawable(context, R.drawable.stacked_card_view_background)
        isClickable = true
        clipToPadding = false
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_VERTICAL
    }

    abstract fun initializeViewModel()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initializeViewModel()
    }
}