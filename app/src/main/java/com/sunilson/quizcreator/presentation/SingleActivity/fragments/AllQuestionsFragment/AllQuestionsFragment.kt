package com.sunilson.quizcreator.presentation.SingleActivity.fragments.AllQuestionsFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentAllQuestionBinding
import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.BaseFragment
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_all_question.view.*
import javax.inject.Inject

class AllQuestionsFragment : BaseFragment() {

    @Inject
    lateinit var questionsRecyclerAdapterFactory: QuestionsRecyclerAdapterFactory

    lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAllQuestionBinding>(inflater, R.layout.fragment_all_question, container, false)
        binding.viewModel = (activity as MainActivity).quizViewModel
        val view = binding.root

        questionsRecyclerAdapter = questionsRecyclerAdapterFactory.create(View.OnClickListener {

        })

        val adapter = ScaleInAnimationAdapter(questionsRecyclerAdapter)
        adapter.setInterpolator(OvershootInterpolator())
        adapter.setFirstOnly(true)
        view.question_recyclerview.adapter = adapter
        view.question_recyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel.loadQuestions()
    }

    companion object {
        fun newInstance(): AllQuestionsFragment = AllQuestionsFragment()
    }

}