package com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.getAnswers
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput.EditTextWithVoiceInput
import kotlinx.android.synthetic.main.all_questions_list_item.view.*
import javax.inject.Inject

class QuestionsRecyclerAdapter(
        context: Context,
        val onDeleteCallback: (Question) -> Unit,
        val saveQuestionCallback: (Question) -> Unit,
        recyclerView: RecyclerView)
    : BaseRecyclerAdapter<Question>(context, recyclerView) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.all_questions_list_item,
                parent,
                false
        )

        val holder = ViewHolder(binding)

        binding.root.question_list_item_top_bar.setOnClickListener {
            val originalQuestion = data[holder.adapterPosition]
            originalQuestion.answers.forEachIndexed { index, answer ->
                val answerView = EditTextWithVoiceInput(context, index > 3, originalQuestion.type == QuestionType.MULTIPLE_CHOICE, answer)
                binding.root.question_list_item_answers.addView(answerView)
            }
            slideToggle(
                    binding.root.question_list_item_content,
                    holder.adapterPosition,
                    binding.root.question_list_item_content.visibility == View.GONE,
                    answerCount = originalQuestion.answers.size,
                    endCallback = {}
            )
        }

        binding.root.question_list_item_answers_save.setOnClickListener {
            val originalQuestion = data[holder.adapterPosition]
            originalQuestion.answers = binding.root.question_list_item_answers.getAnswers()
            slideToggle(binding.root.question_list_item_content, holder.adapterPosition, endCallback = {
                Handler().postDelayed({
                    saveQuestionCallback(originalQuestion)
                }, 250)
            })
        }

        binding.root.question_list_item_answers_delete.setOnClickListener {
            slideToggle(binding.root.question_list_item_content, holder.adapterPosition, endCallback = {
                Handler().postDelayed({
                    onDeleteCallback(data[holder.adapterPosition])
                }, 250)
            })
        }

        return holder
    }

    private fun slideToggle(view: View, position: Int, collapsed: Boolean = false, animation: Boolean = true, answerCount: Int = 0, endCallback: (() -> Unit)? = null) {

        val height =
                if (!collapsed) 0
                else context.resources.getDimensionPixelSize(R.dimen.big_button_height) +
                        30.convertToPx(context) +
                        context.resources.getDimensionPixelSize(R.dimen.big_button_height_top_margin) +
                        answerCount *
                        (context.resources.getDimensionPixelSize(R.dimen.voice_edittext_height) +
                                2 * context.resources.getDimensionPixelSize(R.dimen.voice_edittext_margin))

        if (height == 0) view.question_list_item_answers.removeAllViews()

        if (animation) {
            val slideAnimator = ValueAnimator.ofInt(view.height, height).setDuration(200)
            slideAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.height = value
                view.layoutParams = layoutParams
            }

            slideAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    TransitionManager.beginDelayedTransition(view.rootView as ViewGroup, Fade())
                    if (height != 0) {
                        view.visibility = View.VISIBLE
                    } else {
                        view.visibility = View.GONE
                    }
                    recyclerView.smoothScrollToPosition(position)
                    endCallback?.invoke()
                }
            })

            view.visibility = View.INVISIBLE
            slideAnimator.start()
        } else {
            val layoutParams = view.layoutParams
            layoutParams.height = height
            view.layoutParams = layoutParams
            if (height != 0) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}

@FragmentScope
class QuestionsRecyclerAdapterFactory @Inject constructor(private val context: Context) {
    fun create(onDeleteCallback: (Question) -> Unit, saveQuestionCallback: (Question) -> Unit, recyclerView: RecyclerView): QuestionsRecyclerAdapter {
        return QuestionsRecyclerAdapter(context, onDeleteCallback, saveQuestionCallback, recyclerView)
    }
}