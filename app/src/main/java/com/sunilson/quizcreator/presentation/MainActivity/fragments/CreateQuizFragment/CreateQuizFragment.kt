package com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentCreateQuizBinding
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.QuizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import kotlinx.android.synthetic.main.fragment_create_quiz.view.*
import javax.inject.Inject

class CreateQuizFragment : BaseFragment() {

    @Inject
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    @Inject
    lateinit var createQuizViewModel: CreateQuizViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateQuizBinding>(inflater, R.layout.fragment_create_quiz, container, false)
        binding.viewModel = createQuizViewModel
        val view = binding.root
        view.category_quiz_spinner.adapter = categorySpinnerAdapter

        view.generate_quiz_button.setOnClickListener {
            disposable.add(createQuizViewModel.generateQuiz().subscribe({
                val intent = Intent(context, QuizActivity::class.java)
                intent.putExtra("quizId", it.id)
                startActivity(intent)
            }, {

            }))
        }

        return view
    }

    override fun onAttach(context: Context?) {
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