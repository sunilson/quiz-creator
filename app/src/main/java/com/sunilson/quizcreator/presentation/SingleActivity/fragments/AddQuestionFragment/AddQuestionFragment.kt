package com.sunilson.quizcreator.presentation.SingleActivity.fragments.AddQuestionFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.databinding.FragmentAddQuestionBinding
import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.Dialogs.SimpleInputDialog
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput
import kotlinx.android.synthetic.main.fragment_add_question.view.*
import kotlinx.android.synthetic.main.voice_edittext.view.*
import javax.inject.Inject

class AddQuestionFragment : BaseFragment() {

    @Inject
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAddQuestionBinding>(inflater, R.layout.fragment_add_question, container, false)
        binding.viewModel = (activity as MainActivity).quizViewModel
        val view = binding.root

        for (i in 0..3) {
            addAnswerEditText(view)
        }

        view.form_add_answer.setOnClickListener {
            if (view.form_answer_container.childCount < 8) {
                addAnswerEditText(view, true)
            } else {
                Toast.makeText(context, R.string.cant_add_more_answers, Toast.LENGTH_LONG).show()
            }
        }

        view.form_category_spinner.adapter = categorySpinnerAdapter

        view.form_add_category.setOnClickListener {
            val dialog = SimpleInputDialog.newInstance(getString(R.string.category_name))
            dialog.listener = object : DialogListener<String> {
                override fun onResult(result: String?) {
                    result?.let {
                        disposable.add(viewModel.addCategory(result).subscribe({
                            context?.showToast(getString(R.string.category_add_success))
                        }, {
                            context?.showToast(it.message)
                        }))
                    }
                }
            }
            dialog.show(fragmentManager, "dialog")
        }

        view.form_save_and_exit.setOnClickListener {

            for (i in 0 until view.form_answer_container.childCount) {
                val answerEditText = view.form_answer_container.getChildAt(i) as EditTextWithVoiceInput
                viewModel.creationQuestion.answers.add(Answer(text = answerEditText.text, correctAnswer = i == 0))
            }

            disposable.add(viewModel.createQuestion().subscribe({
                mainActivity.pop()
            }, {}))
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel.loadCategories()
        viewModel.startQuestionCreation()
    }

    fun addAnswerEditText(view: View, transition: Boolean = false) {
        val answerView = EditTextWithVoiceInput(context!!, view.form_answer_container.childCount >= 4)
        val layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 10.convertToPx(context!!)
        layoutParams.bottomMargin = 10.convertToPx(context!!)
        answerView.layoutParams = layoutParams
        if (transition) android.transition.TransitionManager.beginDelayedTransition(view.form_container, ChangeBounds())
        view.form_answer_container.addView(answerView)
        answerView.delete_button.setOnClickListener {
            android.transition.TransitionManager.beginDelayedTransition(view.form_container, ChangeBounds())
            view.form_answer_container.removeView(answerView)
        }
    }

    companion object {
        fun newInstance(): AddQuestionFragment {
            return AddQuestionFragment()
        }
    }
}