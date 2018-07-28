package com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.databinding.FragmentAddQuestionBinding
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.Dialogs.SimpleInputDialog
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.getAnswers
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput.EditTextWithVoiceInput
import kotlinx.android.synthetic.main.fragment_add_question.view.*
import javax.inject.Inject

class AddQuestionFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: AddQuestionViewModel

    @Inject
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAddQuestionBinding>(inflater, R.layout.fragment_add_question, container, false)
        binding.viewModel = viewModel
        val view = binding.root

        view.form_add_answer.setOnClickListener {
            if (view.form_answer_container.childCount < 8) {
                addAnswerEditText(view, true)
            } else {
                Toast.makeText(context, R.string.cant_add_more_answers, Toast.LENGTH_LONG).show()
            }
        }
        view.form_category_spinner.adapter = categorySpinnerAdapter
        view.form_add_category.setOnClickListener {
            val dialog = SimpleInputDialog.newInstance(getString(R.string.add_category))
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
            viewModel.creationQuestion.answers.clear()
            viewModel.creationQuestion.answers = view.form_answer_container.getAnswers()
            disposable.add(viewModel.createQuestion().subscribe({
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }, {}))
        }
        view.form_save_and_continue.setOnClickListener {
            viewModel.creationQuestion.answers.clear()
            viewModel.creationQuestion.answers = view.form_answer_container.getAnswers()
            disposable.add(viewModel.createQuestion().subscribe({
                (activity!! as AddQuestionActivity).restart(this)
            }, {}))
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel.loadCategories()
        viewModel.startQuestionCreation(arguments?.getSerializable("type") as QuestionType)
    }

    fun addAnswerEditText(view: View, transition: Boolean = false) {
        val answerView = EditTextWithVoiceInput(
                context!!,
                view.form_answer_container.childCount > 3,
                arguments?.getSerializable("type") as QuestionType == QuestionType.MULTIPLE_CHOICE,
                Answer(text =  getString(R.string.answer_default_text)))
        if (transition) android.transition.TransitionManager.beginDelayedTransition(view.form_container, ChangeBounds())
        view.form_answer_container.addView(answerView)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    companion object {
        fun newInstance(type: QuestionType): AddQuestionFragment {
            val bundle = Bundle()
            bundle.putSerializable("type", type)
            val fragment = AddQuestionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}