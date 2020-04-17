package com.sunilson.quizcreator.presentation.quizActivity.fragments.singleQuizFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunilson.quizcreator.databinding.FragmentSingleQuizBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.QuizViewModel
import kotlinx.android.synthetic.main.fragment_single_quiz.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class SingleQuizFragment : BaseFragment() {

    private val singleQuizRecyclerAdapter: SingleQuizRecyclerAdapter by inject()
    private val viewModel: QuizViewModel by lazy { requireActivity().lifecycleScope.get<QuizViewModel>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSingleQuizBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val view = binding.root

        view.single_quiz_recyclerview.adapter = singleQuizRecyclerAdapter
        view.single_quiz_recyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }

    companion object {
        fun newInstance(): SingleQuizFragment {
            return SingleQuizFragment()
        }
    }
}