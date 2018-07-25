package com.sunilson.quizcreator.presentation.QuizActivity.fragments.ResultFragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_quiz_result.view.*

class ResultFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_quiz_result, container, false)

        Handler().postDelayed({
            view.result_checkmark_animation.playAnimation()

            val headlineAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_and_slide_up)
            val subtitleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
            val buttonAnimationLeft = AnimationUtils.loadAnimation(context, R.anim.fade_and_slide_in_left)
            val buttonAnimationRight = AnimationUtils.loadAnimation(context, R.anim.fade_and_slide_in_right)

            headlineAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {
                    view.result_headline.visibility = View.VISIBLE
                }

            })
            headlineAnimation.startOffset = 1500
            view.result_headline.startAnimation(headlineAnimation)

            subtitleAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {
                    view.result_count.visibility = View.VISIBLE
                }
            })
            subtitleAnimation.startOffset = 1800
            view.result_count.startAnimation(subtitleAnimation)

            buttonAnimationLeft.startOffset = 2500
            buttonAnimationRight.startOffset = 2500
            buttonAnimationLeft.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {
                    view.button_exit_quiz.visibility = View.VISIBLE
                }
            })
            buttonAnimationRight.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {
                    view.button_view_questions.visibility = View.VISIBLE
                }
            })
            view.button_exit_quiz.startAnimation(buttonAnimationLeft)
            view.button_view_questions.startAnimation(buttonAnimationRight)

        }, 500)

        view.button_exit_quiz.setOnClickListener {
            activity?.finish()
        }

        view.button_view_questions.setOnClickListener {

        }

        return view
    }

    companion object {
        fun newInstance(): ResultFragment {
            return ResultFragment()
        }
    }
}