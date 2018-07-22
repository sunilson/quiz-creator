package com.sunilson.quizcreator.presentation.views.StackedCardsView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.clamp
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import com.sunilson.quizcreator.presentation.views.QuestionCardView.QuestionCardView


class StackedCardsView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    //Attrs
    private val topAndBottomSpacing: Int
    private val maxChildSpacing: Int
    private val maxChildHeight: Int
    private val orderAnimationDuration: Int
    private val addAnimationDuration: Int
    private val popAnimationDuration: Int
    private val addAnimationType: StackAnimationTypes

    //Private properties
    private var availableHeight: Int = 0
    private var childHeight: Int = 0
    private var halfChildHeight: Int = 0
    private var maxContentheight: Int = 0
    private var halfHeight: Int = 0
    private var childSpacing: Int = 0
    private var firstMargin: Int = 0
    private var currentPopAnimation: ViewPropertyAnimator? = null
    private var currentPopTarget: View? = null
    private var currentAddAnimation: Animation? = null
    private var currentAddTarget: View? = null

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.StackedCardsView, 0, 0)
        topAndBottomSpacing = a.getInt(R.styleable.StackedCardsView_topAndBottomSpacing, 50)
        maxChildHeight = a.getInt(R.styleable.StackedCardsView_maxChildHeight, 400.convertToPx(context))
        maxChildSpacing = a.getInt(R.styleable.StackedCardsView_topAndBottomSpacing, 50.convertToPx(context))
        orderAnimationDuration = a.getInt(R.styleable.StackedCardsView_orderAnimationDuration, 500)
        addAnimationDuration = a.getInt(R.styleable.StackedCardsView_addAnimationDuration, 500)
        popAnimationDuration = a.getInt(R.styleable.StackedCardsView_popAnimationDuration, 500)
        addAnimationType = StackAnimationTypes.values()[a.getInt(R.styleable.StackedCardsView_addAnimationType, 0)]
        a.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (availableHeight == 0) {
            availableHeight = height - paddingTop - paddingBottom
            halfHeight = availableHeight / 2
            childHeight = (availableHeight - 50.convertToPx(context)).clamp(0, maxChildHeight)
            halfChildHeight = childHeight / 2
            maxContentheight = availableHeight - topAndBottomSpacing
        }
    }

    fun addListOfCards(list: List<View>) {
        removeAllViews()
        list.forEach {
            addView(setupView(it), 0)
        }
        Handler().postDelayed({
            calculateChildMargin()
            reorder()
        }, 500)
    }

    fun addCardBehind(view: View) {
        currentAddTarget = setupView(view)

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

    private fun setupView(view: View): View {
        val layoutParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, childHeight)
        view.layoutParams = layoutParams
        return view
    }

    fun removeCard(animationTypes: StackAnimationTypes) {

        if (childCount != 0) {
            if (currentPopAnimation != null && currentPopTarget != null && currentPopAnimation != null) {
                currentPopAnimation?.cancel()
                removeView(currentPopTarget)
            }

            currentPopTarget = getChildAt(this.childCount - 1)
            currentPopAnimation = currentPopTarget!!.animate()

            when (animationTypes) {
                StackAnimationTypes.TRANSLATE_DOWN -> {
                    val animationDistance = height - (currentPopTarget?.layoutParams as RelativeLayout.LayoutParams).topMargin
                    currentPopAnimation = currentPopAnimation!!.translationY(animationDistance.toFloat())
                }
                StackAnimationTypes.TRANSLATE_UP -> {
                    val animationDistance = height - (currentPopTarget?.layoutParams as RelativeLayout.LayoutParams).topMargin
                    currentPopAnimation = currentPopAnimation!!.translationY(-animationDistance.toFloat())
                }
                StackAnimationTypes.TRANSLATE_LEFT -> {
                    val animationDistance = width.toFloat() * 1.5f
                    currentPopAnimation = currentPopAnimation!!.translationX(-animationDistance)
                    currentPopAnimation = currentPopAnimation!!.rotationBy(-40f)
                }
                StackAnimationTypes.TRANSLATE_RIGHT -> {
                    val animationDistance = width.toFloat() * 1.5f
                    currentPopAnimation = currentPopAnimation!!.translationX(animationDistance)
                    currentPopAnimation = currentPopAnimation!!.rotationBy(40f)
                }
            }

            currentPopAnimation!!
                    .withLayer()
                    .setDuration(popAnimationDuration.toLong())
                    .setInterpolator(AccelerateInterpolator())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            removeView(currentPopTarget)
                            calculateChildMargin()
                            reorder()
                            currentPopTarget = null
                            currentPopAnimation = null
                        }
                    })
                    .start()
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
            val child = getChildAt(i) as QuestionCardView
            child
                    .animate()
                    .translationY((firstMargin + i * childSpacing).toFloat())
                    .withLayer()
                    .setDuration(orderAnimationDuration.toLong())
                    .start()
            //child.translationZ = i.toFloat()
        }
    }
}