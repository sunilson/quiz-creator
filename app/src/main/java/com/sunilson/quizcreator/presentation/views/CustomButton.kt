package com.sunilson.quizcreator.presentation.views

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.extensions.convertToPx
import kotlinx.android.synthetic.main.custom_button.view.*

class CustomButton(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    var type: ButtonType = ButtonType.DEFAULT
        set(value) {
            field = value
            if (!iconOnly) {
                val drawable = when (field) {
                    ButtonType.DEFAULT -> ContextCompat.getDrawable(context, R.drawable.button_default_background)
                    ButtonType.CONFIRM -> ContextCompat.getDrawable(context, R.drawable.button_confirm_background)
                    ButtonType.CANCEL -> ContextCompat.getDrawable(context, R.drawable.button_cancel_background)
                }

                if (drawable != null) background = drawable
            }
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

    var small: Boolean = false
        set(value) {
            field = value
            if (value) {
                custom_button_icon.layoutParams.height = 15.convertToPx(context)
                custom_button_icon.layoutParams.width = 15.convertToPx(context)
                val padding = 6.convertToPx(context)
                custom_button_container.setPadding(padding, padding, padding, padding)
            } else {
                custom_button_icon.layoutParams.height = 20.convertToPx(context)
                custom_button_icon.layoutParams.width = 20.convertToPx(context)
                val padding = 12.convertToPx(context)
                custom_button_container.setPadding(padding, padding, padding, padding)
            }
        }

    var iconOnly: Boolean = false
        set(value) {
            field = value
            if (field) {
                background = ContextCompat.getDrawable(context, R.drawable.button_empty_background)
                val color = when (type) {
                    ButtonType.DEFAULT -> ContextCompat.getColor(context, R.color.button_default_background)
                    ButtonType.CONFIRM -> ContextCompat.getColor(context, R.color.button_confirm_background)
                    ButtonType.CANCEL -> ContextCompat.getColor(context, R.color.button_cancel_background)
                }
                custom_button_icon.setColorFilter(color)
            }
            else {
                custom_button_icon.setColorFilter(Color.WHITE)
                type = type
            }
        }

    var loading: Boolean = false
        set(value) {
            field = value
        }

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
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
        buttonText = a.getString(R.styleable.CustomButton_buttonText).orEmpty()
        small = a.getBoolean(R.styleable.CustomButton_small, false)
        iconOnly = a.getBoolean(R.styleable.CustomButton_iconOnly, false)
        a.recycle()
    }
}

enum class ButtonType {
    DEFAULT, CONFIRM, CANCEL
}