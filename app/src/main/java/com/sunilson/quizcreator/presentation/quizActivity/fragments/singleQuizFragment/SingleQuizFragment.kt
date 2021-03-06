package com.sunilson.quizcreator.presentation.quizActivity.fragments.singleQuizFragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.databinding.FragmentSingleQuizBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.QuizViewModel
import kotlinx.android.synthetic.main.fragment_single_quiz.view.*
import javax.inject.Inject

class SingleQuizFragment : BaseFragment() {

    @Inject
    lateinit var singleQuizRecyclerAdapter: SingleQuizRecyclerAdapter

    val viewModel: QuizViewModel by lazy { (activity!! as QuizActivity).quizViewModel }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSingleQuizBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val view = binding.root

        view.single_quiz_recyclerview.adapter = singleQuizRecyclerAdapter
        view.single_quiz_recyclerview.layoutManager =
            LinearLayoutManager(context)

        return view
    }

    companion object {
        fun newInstance(): SingleQuizFragment {
            return SingleQuizFragment()
        }
    }
}