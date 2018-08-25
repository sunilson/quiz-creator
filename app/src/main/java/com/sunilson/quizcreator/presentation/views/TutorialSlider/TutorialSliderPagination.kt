package com.sunilson.quizcreator.presentation.views.TutorialSlider

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.sunilson.quizcreator.R

class TutorialSliderPagination(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    var itemAmount: Int = 0
        set(value) {
            field = value
            removeAllViews()
            for (i in 0 until value) {
                val view = LinearLayout(context)
                val layoutParams = LinearLayout.LayoutParams(35, 35)
                layoutParams.setMargins(15, 0, 15, 0)
                view.layoutParams = layoutParams
                view.background = ContextCompat.getDrawable(context, R.drawable.pagination_select_circle_transition)
                view.setOnClickListener { this.listener?.invoke(i) }
                addView(view)
            }
            invalidate()
            requestLayout()
            this.activeItem = 0
        }

    var activeItem: Int = 0
        set(value) {
            if (value == 0 && field == 0) {
                (getChildAt(0).background as TransitionDrawable).startTransition((300))
            } else if (field != value) {
                (getChildAt(value).background as TransitionDrawable).startTransition(300)
                (getChildAt(field).background as TransitionDrawable).reverseTransition(300)
            }

            field = value
        }

    private var listener: TutorialPaginationListener? = null
    fun setPaginationItemClickedListener(listener: TutorialPaginationListener) {
        this.listener = listener
    }
    /*
    private var lastScrollValue = 0f
    private var direction: Direction = Direction.NONE
    fun animate(value: Float, selected: Int) {
        /*
        when {
            value == 0f -> {
                direction = Direction.NONE
            }
            lastScrollValue == 0f -> {
                direction = if (value > 0.5f) Direction.LEFT else Direction.RIGHT
            }
            else -> {
                direction = when {
                    value > lastScrollValue -> Direction.RIGHT
                    value < lastScrollValue -> Direction.LEFT
                    else -> {
                        Direction.NONE
                    }
                }
            }
        }
        lastScrollValue = value
        */
    }
    */

    init {
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.EditTextWithVoiceInput, 0, 0)
        itemAmount = a.getInt(R.styleable.TutorialSliderPagination_itemAmount, 5)
        a.recycle()
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }

    private enum class Direction {
        LEFT, RIGHT, NONE
    }
}

typealias  TutorialPaginationListener = (Int) -> Unit