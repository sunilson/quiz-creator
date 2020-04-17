package com.sunilson.quizcreator.presentation.views.quizView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.AudioService
import com.sunilson.quizcreator.presentation.shared.stack_fade_in_duration
import com.sunilson.quizcreator.presentation.shared.timer_fade_delay_between
import com.sunilson.quizcreator.presentation.shared.timer_fade_half_duration
import com.sunilson.quizcreator.presentation.shared.timer_fade_scale_duration
import com.sunilson.quizcreator.presentation.views.questionCardView.MultipleChoiceQuestionCardView
import com.sunilson.quizcreator.presentation.views.questionCardView.SingleChoiceQuestionCardView
import com.sunilson.quizcreator.presentation.views.stackedCardsView.StackAnimationTypes
import kotlinx.android.synthetic.main.quiz_view.view.*
import java.util.*


class QuizView(context: Context, val attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var nextCardDelay: Int = 0
    var quizFinishedCallback: (() -> Unit)? = null
    var audioService: AudioService? = null

    var quiz: Quiz? = null
        set(value) {
            value?.let {
                val animators1 = ObjectAnimator.ofFloat(start_timer, "scaleX", 0.5f, 1.5f)
                val animators2 = ObjectAnimator.ofFloat(start_timer, "scaleY", 0.5f, 1.5f)
                val animator1 = ObjectAnimator.ofFloat(start_timer, "alpha", 0f, 1f)
                val animator2 = ObjectAnimator.ofFloat(start_timer, "alpha", 1f, 0f)

                animator1.duration = timer_fade_half_duration
                animator2.duration = timer_fade_half_duration
                animators1.duration = timer_fade_scale_duration
                animators2.duration = timer_fade_scale_duration

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animators1, animators2)
                animatorSet.play(animator2).after(timer_fade_delay_between).after(animator1)
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        val timerValue = start_timer.text.toString().toInt()
                        if (timerValue > 1) {
                            start_timer.text = (timerValue - 1).toString()
                            animatorSet.start()
                        } else {
                            start_timer.visibility = View.GONE
                            val alphaAnimator = ObjectAnimator.ofFloat(stack_view, "alpha", 0f, 1f)
                            alphaAnimator.duration = stack_fade_in_duration
                            alphaAnimator.start()

                            field = value
                            quiz?.timestamp = Date().time
                            stack_view.addListOfCards(it.questions.mapIndexed { index, question ->
                                when (question.type) {
                                    QuestionType.SINGLE_CHOICE -> {
                                        SingleChoiceQuestionCardView(context, question) {
                                            handleQuestionClicked(it, question)
                                        }
                                    }
                                    else -> {
                                        MultipleChoiceQuestionCardView(context, question) {
                                            handleQuestionClicked(it, question)
                                        }
                                    }
                                }
                            })
                        }
                    }
                })
                animatorSet.start()
            }
        }

    private fun handleQuestionClicked(correct: Boolean, question: Question) {
        if (correct) {
            audioService?.playCorrectSound()
            quiz!!.correctAnswers++
            question.correctlyAnswered = true
        } else {
            audioService?.playErrorSound()
        }
        Handler().postDelayed({
            stack_view.removeCard(if (correct) StackAnimationTypes.TRANSLATE_RIGHT else StackAnimationTypes.TRANSLATE_LEFT) {
                checkFinished()
            }
            checkFinished()
        }, nextCardDelay.toLong())
    }

    private fun checkFinished() {
        if (stack_view.childCount == 0) {
            quiz!!.finished = true
            quiz!!.duration = Date().time - quiz!!.timestamp
            quizFinishedCallback?.invoke()
        }
    }


    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.QuizView, 0, 0)
        nextCardDelay = a.getInt(R.styleable.QuizView_nextCardDelay, 1500)
        a.recycle()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.quiz_view, this, true)
    }
}