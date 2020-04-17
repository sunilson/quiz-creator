package com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentCreateQuizBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.numberSelectionDialog.NumberSelectionDialog
import kotlinx.android.synthetic.main.fragment_create_quiz.view.*
import org.koin.androidx.scope.lifecycleScope

class CreateQuizFragment : BaseFragment() {

    private val categorySpinnerAdapter: CategorySpinnerAdapter by lifecycleScope.inject()
    private val quizArchiveRecyclerAdapterFactory: QuizArchiveRecyclerAdapterFactory by lifecycleScope.inject()
    private val createQuizViewModel: CreateQuizViewModel by lifecycleScope.inject()

    private lateinit var adapter: QuizArchiveRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateQuizBinding>(
            inflater,
            R.layout.fragment_create_quiz,
            container,
            false
        )
        binding.viewModel = createQuizViewModel
        val view = binding.root
        view.category_quiz_spinner.adapter = categorySpinnerAdapter

        view.generate_quiz_button.setOnClickListener {
            val intent = Intent(context, QuizActivity::class.java)
            if (createQuizViewModel.selectedCategory.get() != null && createQuizViewModel.selectedCategory.get()!!.id != "all") {
                intent.putExtra("selectedCategory", createQuizViewModel.selectedCategory.get()!!.id)
            }
            intent.putExtra("shuffleAnswers", createQuizViewModel.shuffleAnswers.get())
            intent.putExtra("onlySingle", createQuizViewModel.singleOrMultiple.get())
            intent.putExtra("maxQuestionAmount", createQuizViewModel.maxQuestionAmount.get())
            startActivity(intent)
        }

        view.select_question_amount_button.setOnClickListener {
            val dialog = NumberSelectionDialog.newInstance(
                getString(R.string.max_quiz_questions),
                createQuizViewModel.maxQuestionAmount.get()
            )
            dialog.listener = object : DialogListener<Int> {
                override fun onResult(result: Int?) {
                    if (result != null) {
                        createQuizViewModel.maxQuestionAmount.set(if (result > 0) result else 1)
                    }
                }
            }
            dialog.show(childFragmentManager, "dialog")
        }

        adapter = quizArchiveRecyclerAdapterFactory.create({
            val intent = Intent(activity, QuizActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }, view.quiz_archive_recyclerview)
        view.quiz_archive_recyclerview.layoutManager = LinearLayoutManager(context)
        view.quiz_archive_recyclerview.adapter = adapter

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createQuizViewModel.loadCategories()
    }

    override fun onDestroy() {
        super.onDestroy()
        createQuizViewModel.onDestroy()
    }

    companion object {
        fun newInstance(): CreateQuizFragment = CreateQuizFragment()
    }
}