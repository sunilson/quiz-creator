package com.sunilson.quizcreator.presentation.addQuestionActivity.fragments

import android.content.Context
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.databinding.FragmentAddQuestionBinding
import com.sunilson.quizcreator.presentation.addQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.ANSWERS_PER_QUIZ_QUESTION
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import com.sunilson.quizcreator.presentation.shared.MAX_ANSWERS_PER_QUESTION
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleInputDialog
import com.sunilson.quizcreator.presentation.shared.dialogs.numberSelectionDialog.NumberSelectionDialog
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.getAnswers
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.showToast
import com.sunilson.quizcreator.presentation.views.editTextWithVoiceInput.EditTextWithVoiceInput
import kotlinx.android.synthetic.main.fragment_add_question.*
import kotlinx.android.synthetic.main.fragment_add_question.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class AddQuestionFragment : BaseFragment() {

    private val eventBus: EventBus by inject()
    private val viewModel: AddQuestionViewModel by lifecycleScope.inject()
    private val categorySpinnerAdapter: CategorySpinnerAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_question_menu, menu)
        if (arguments?.getString("id") != null) {
            menu.findItem(R.id.add_question_save_continue)?.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_question_save -> {
                saveAndExit()
            }
            R.id.add_question_save_continue -> {
                saveAndContinue()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddQuestionBinding>(
            inflater,
            R.layout.fragment_add_question,
            container,
            false
        )
        binding.viewModel = viewModel
        val view = binding.root

        view.form_add_answer.setOnClickListener {
            if (view.form_answer_container.childCount < MAX_ANSWERS_PER_QUESTION) {
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
            dialog.show(childFragmentManager, "dialog")
        }

        view.max_answers_value.setOnClickListener {
            val dialog = NumberSelectionDialog.newInstance(
                getString(R.string.max_answer_amount),
                viewModel.question!!.maxAnswers,
                MAX_ANSWERS_PER_QUESTION
            )
            dialog.listener = object : DialogListener<Int> {
                override fun onResult(result: Int?) {
                    if (result != null) {
                        when {
                            result < view.form_answer_container.getAnswers().size -> {
                                context?.showToast(getString(R.string.set_max_answer_error))
                                viewModel.question!!.maxAnswers =
                                    view.form_answer_container.getAnswers().size
                            }
                            result < ANSWERS_PER_QUIZ_QUESTION -> {
                                context?.showToast(getString(R.string.max_answers_minimum))
                                viewModel.question!!.maxAnswers = ANSWERS_PER_QUIZ_QUESTION
                            }
                            else -> viewModel.question!!.maxAnswers = result
                        }
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }

        /* view.form_save_and_exit.setOnClickListener {
             saveAndExit()
         }

         view.form_save_and_continue.setOnClickListener {
             saveAndContinue()
         }*/

        return view
    }

    private fun saveAndContinue() {
        viewModel.question?.answers?.clear()
        viewModel.question?.answers = form_answer_container.getAnswers()
        disposable.add(viewModel.createQuestion().subscribe({
            eventBus.publishToChannel(EventChannel.RELOAD_QUESTIONS, null)
            eventBus.publishToChannel(EventChannel.RELOAD_CATEGORIES, null)
            (activity!! as AddQuestionActivity).restart(this)
        }, {
            context?.showToast(it.message)
        }))
    }

    private fun saveAndExit() {
        viewModel.question?.answers?.clear()
        viewModel.question?.answers = form_answer_container.getAnswers()
        disposable.add(viewModel.createQuestion().subscribe({
            eventBus.publishToChannel(EventChannel.RELOAD_QUESTIONS, null)
            eventBus.publishToChannel(EventChannel.RELOAD_CATEGORIES, null)
            activity?.finish()
        }, {
            context?.showToast(it.message)
        }))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.loadCategories()
        if (arguments?.getString("id") != null) {
            viewModel.loadQuestion(arguments?.getString("id")!!)
        } else {
            viewModel.startQuestionCreation(arguments?.getSerializable("type") as QuestionType)
        }
    }

    private fun addAnswerEditText(view: View, transition: Boolean = false) {
        val answerView = EditTextWithVoiceInput(
            context!!,
            view.form_answer_container.childCount > 0,
            viewModel.question!!.type == QuestionType.MULTIPLE_CHOICE,
            Answer(text = getString(R.string.answer_default_text))
        )
        if (transition) android.transition.TransitionManager.beginDelayedTransition(
            view.form_container,
            ChangeBounds()
        )
        view.form_answer_container.addView(answerView)
        view.form_container.postDelayed({
            view.form_container.fullScroll(ScrollView.FOCUS_DOWN)
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    companion object {
        fun newInstance(
            type: QuestionType? = QuestionType.SINGLE_CHOICE,
            id: String? = null
        ): AddQuestionFragment {
            val bundle = Bundle()
            if (id != null) bundle.putString("id", id)
            else bundle.putSerializable("type", type)
            val fragment = AddQuestionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}