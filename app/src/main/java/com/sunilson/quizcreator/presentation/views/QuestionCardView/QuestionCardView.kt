package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx

abstract class QuestionCardView(context: Context,
                                val question: Question,
                                val questionSubmitted: (Boolean) -> Unit) : ConstraintLayout(context) {

    protected val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
        setPadding(10.convertToPx(context), 15.convertToPx(context), 2.convertToPx(context), 10.convertToPx(context))
        background = ContextCompat.getDrawable(context, R.drawable.stacked_card_view_background)
        isClickable = true
    }

    abstract fun initializeViewModel()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initializeViewModel()
    }
}