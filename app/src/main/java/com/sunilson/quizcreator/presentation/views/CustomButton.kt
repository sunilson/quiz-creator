package com.sunilson.quizcreator.presentation.views

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import kotlinx.android.synthetic.main.custom_button.view.*

class CustomButton : FrameLayout {

    var type: ButtonType = ButtonType.DEFAULT
        set(value) {
            field = value

            val drawable = when (field) {
                ButtonType.DEFAULT -> ContextCompat.getDrawable(context, R.drawable.button_default_background)
                ButtonType.CONFIRM -> ContextCompat.getDrawable(context, R.drawable.button_confirm_background)
                ButtonType.CANCEL -> ContextCompat.getDrawable(context, R.drawable.button_cancel_background)
            }

            if (drawable != null) background = drawable
        }

    var buttonText: String = ""
        set(value) {
            field = value
            custom_button_text.text = value
            if (field.isEmpty()) custom_button_text.visibility = View.GONE
            else {
                val params = custom_button_text.layoutParams as LinearLayout.LayoutParams
                if (icon != null) params.leftMargin = 7.convertToPx(context)
                else params.leftMargin = 0
                custom_button_text.layoutParams = params
            }
        }

    var icon: Drawable? = null
        set(value) {
            field = value
            if (field != null) {
                custom_button_icon.setImageDrawable(field)
            } else {
                custom_button_icon.visibility = View.GONE
            }
        }

    var loading: Boolean = false
        set(value) {
            field = value
        }

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflater.inflate(R.layout.custom_button, this, true)
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomButton, 0, 0)
        type = when (a.getInt(R.styleable.CustomButton_type, 0)) {
            0 -> ButtonType.DEFAULT
            1 -> ButtonType.CONFIRM
            else -> ButtonType.CANCEL
        }

        clipToPadding = false
        elevation = 10f
        stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.button_animator)

        icon = a.getDrawable(R.styleable.CustomButton_buttonIcon)
        buttonText = if (a.getString(R.styleable.CustomButton_buttonText) != null) a.getString(R.styleable.CustomButton_buttonText)
        else ""
        a.recycle()
    }
}

enum class ButtonType {
    DEFAULT, CONFIRM, CANCEL
}