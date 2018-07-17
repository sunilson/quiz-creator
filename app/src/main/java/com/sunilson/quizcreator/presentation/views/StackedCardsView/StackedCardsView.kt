package com.sunilson.quizcreator.presentation.views.StackedCardsView

import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.clamp
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx


class StackedCardsView : RelativeLayout {

    //Attrs
    private val topAndBottomSpacing: Int
    private val maxChildSpacing: Int
    private val maxChildHeight: Int
    private val orderAnimationDuration: Int
    private val addAnimationDuration: Int
    private val popAnimationDuration: Int
    private val addAnimationType: StackAnimationTypes
    private val popAnimationType: StackAnimationTypes

    //Private properties
    private var availableHeight: Int = 0
    private var childHeight: Int = 0
    private var halfChildHeight: Int = 0
    private var maxContentheight: Int = 0
    private var halfHeight: Int = 0
    private var childSpacing: Int = 0
    private var firstMargin: Int = 0

    private var currentPopAnimation: Animation? = null
    private var currentPopTarget: View? = null

    private var currentAddAnimation: Animation? = null
    private var currentAddTarget: CardView? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.StackedCardsView, 0, 0)
        topAndBottomSpacing = a.getInt(R.styleable.StackedCardsView_topAndBottomSpacing, 50)
        maxChildHeight = a.getInt(R.styleable.StackedCardsView_maxChildHeight, 300.convertToPx(context))
        maxChildSpacing = a.getInt(R.styleable.StackedCardsView_topAndBottomSpacing, 50.convertToPx(context))
        orderAnimationDuration = a.getInt(R.styleable.StackedCardsView_orderAnimationDuration, 1000)
        addAnimationDuration = a.getInt(R.styleable.StackedCardsView_addAnimationDuration, 500)
        popAnimationDuration = a.getInt(R.styleable.StackedCardsView_popAnimationDuration, 500)
        popAnimationType = StackAnimationTypes.values()[a.getInt(R.styleable.StackedCardsView_popAnimationType, 1)]
        addAnimationType = StackAnimationTypes.values()[a.getInt(R.styleable.StackedCardsView_addAnimationType, 0)]
        a.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        availableHeight = height - paddingTop - paddingBottom
        halfHeight = availableHeight / 2
        childHeight = (availableHeight - 50.convertToPx(context)).clamp(0, maxChildHeight)
        halfChildHeight = childHeight / 2
        maxContentheight = availableHeight - topAndBottomSpacing
    }

    fun addCardBehind() {
        currentAddTarget = CardView(context)
        currentAddTarget!!.useCompatPadding = true
        currentAddTarget!!.cardElevation = 15f
        currentAddTarget!!.radius = 10f
        val layoutParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, childHeight)
        currentAddTarget!!.layoutParams = layoutParams

        //If there is a removal in progress, cancel and remove immediately
        if (currentAddAnimation != null && currentAddTarget != null && !currentAddAnimation!!.hasEnded()) {
            currentAddAnimation?.cancel()
        }

        currentAddAnimation = when (addAnimationType) {
            StackAnimationTypes.TRANSLATE_DOWN -> {
                val animationDistance = height
                TranslateAnimation(0f, 0f, -animationDistance.toFloat(), 0f)
            }
            StackAnimationTypes.TRANSLATE_UP -> {
                val animationDistance = height
                TranslateAnimation(0f, 0f, animationDistance.toFloat(), 0f)
            }
            StackAnimationTypes.TRANSLATE_LEFT -> {
                val animationDistance = width.toFloat()
                TranslateAnimation(0f, -animationDistance, 0f, 0f)
            }
            StackAnimationTypes.TRANSLATE_RIGHT -> {
                val animationDistance = width.toFloat()
                TranslateAnimation(0f, animationDistance, 0f, 0f)
            }
            else -> {
                null
            }
        }

        addView(currentAddTarget, 0)
        if (currentAddAnimation != null) {
            currentAddAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    calculateChildMargin()
                    reorder()
                }

                override fun onAnimationStart(p0: Animation?) {}
            })
            currentAddAnimation!!.interpolator = AccelerateInterpolator()
            currentAddAnimation!!.duration = addAnimationDuration.toLong()
            currentAddTarget!!.startAnimation(currentAddAnimation)
        } else {
            calculateChildMargin()
            reorder()
        }
    }

    fun removeCard() {
        //val view = stack.pop()
        if (childCount != 0) {

            //If there is a removal in progress, cancel and remove immediately
            if (currentPopAnimation != null && currentPopTarget != null && !currentPopAnimation!!.hasEnded()) {
                currentPopAnimation?.cancel()
                removeView(currentPopTarget)
            }

            currentPopTarget = getChildAt(this.childCount - 1)
            currentPopAnimation = when (popAnimationType) {
                StackAnimationTypes.TRANSLATE_DOWN -> {
                    val animationDistance = height - (currentPopTarget?.layoutParams as RelativeLayout.LayoutParams).topMargin
                    TranslateAnimation(0f, 0f, 0f, animationDistance.toFloat())
                }
                StackAnimationTypes.TRANSLATE_UP -> {
                    val animationDistance = height - (currentPopTarget?.layoutParams as RelativeLayout.LayoutParams).topMargin
                    TranslateAnimation(0f, 0f, 0f, -animationDistance.toFloat())
                }
                StackAnimationTypes.TRANSLATE_LEFT -> {
                    val animationDistance = width.toFloat()
                    TranslateAnimation(0f, -animationDistance, 0f, 0f)
                }
                StackAnimationTypes.TRANSLATE_RIGHT -> {
                    val animationDistance = width.toFloat()
                    TranslateAnimation(0f, animationDistance, 0f, 0f)
                }
                else -> {
                    null
                }
            }

            if (currentPopAnimation != null) {
                currentPopAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        removeView(currentPopTarget)
                        calculateChildMargin()
                        reorder()
                    }

                    override fun onAnimationStart(p0: Animation?) {}
                })
                currentPopAnimation!!.interpolator = AccelerateInterpolator()
                currentPopAnimation!!.duration = popAnimationDuration.toLong()
                currentPopTarget!!.startAnimation(currentPopAnimation)
            } else {
                removeView(currentPopTarget)
                calculateChildMargin()
                reorder()
            }
        }
    }

    private fun calculateChildMargin() {
        if (childCount != 0) {
            val availableSpace = maxContentheight - childHeight
            childSpacing = (availableSpace / childCount).clamp(0, maxChildSpacing)
            val rectHeight = (childCount - 1) * childSpacing + childHeight
            firstMargin = halfHeight - rectHeight / 2
        }
    }

    private fun reorder() {
        for (i in 0 until childCount) {
            val child = getChildAt(i) as CardView
            val layoutParams = child.layoutParams as RelativeLayout.LayoutParams

            val animation = ValueAnimator.ofInt(layoutParams.topMargin, firstMargin + i * childSpacing)
            animation.addUpdateListener {
                layoutParams.topMargin = it.animatedValue as Int
                child.layoutParams = layoutParams
                child.requestLayout()
            }
            animation.duration = orderAnimationDuration.toLong()
            animation.start()
            child.translationZ = i.toFloat()
        }
    }
}