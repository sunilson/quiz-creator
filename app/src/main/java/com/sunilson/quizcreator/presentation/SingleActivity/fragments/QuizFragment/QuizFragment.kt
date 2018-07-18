package com.sunilson.quizcreator.presentation.SingleActivity.fragments.QuizFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentQuizBinding
import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import javax.inject.Inject

class QuizFragment : BaseFragment() {

    @Inject
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater, R.layout.fragment_quiz, container, false)
        binding.viewModel = (activity as MainActivity).quizViewModel
        val view = binding.root

        view.category_quiz_spinner.adapter = categorySpinnerAdapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel.loadCategories()
    }

    companion object {
        fun newInstance() : QuizFragment = QuizFragment()
    }

}