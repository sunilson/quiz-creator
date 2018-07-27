package com.sunilson.quizcreator.presentation.views.CustomProgressBar

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.sunilson.quizcreator.R
import kotlinx.android.synthetic.main.custom_progress_bar.view.*

class CustomProgressBar : FrameLayout {

    var progress: Float = 0f
        set(value) {
            if (!(value == 0f && field == 0f)) {
                val objectAnimator = ObjectAnimator
                        .ofInt(progress_bar, "progress", field.toInt() * 1000, value.toInt() * 1000)
                        .setDuration(5000)
                objectAnimator.interpolator = DecelerateInterpolator()
                objectAnimator.addUpdateListener {
                    progress_text.text = (it.animatedValue as Int / 1000).toString()
                }
                objectAnimator.start()
            }
            field = value
        }

    var title: String = ""
        set(value) {
            field = value
            title_text.text = value
        }


    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflater.inflate(R.layout.custom_progress_bar, this, true)
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomProgressBar, 0, 0)
        title = a.getString(R.styleable.CustomProgressBar_progress_label)
        a.recycle()
        progress_bar.max = 100000
        progress_bar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.correct))
    }

    constructor(context: Context, title: String, value: Float) : super(context) {
        inflater.inflate(R.layout.custom_progress_bar, this, true)
        this.title = title
        this.progress = value
        progress_bar.max = 100000
        progress_bar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.correct))
    }


}