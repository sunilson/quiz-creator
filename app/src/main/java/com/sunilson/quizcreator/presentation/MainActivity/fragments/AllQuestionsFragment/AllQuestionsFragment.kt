package com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentAllQuestionBinding
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_all_question.view.*
import javax.inject.Inject

class AllQuestionsFragment : BaseFragment() {

    @Inject
    lateinit var questionsRecyclerAdapterFactory: QuestionsRecyclerAdapterFactory

    @Inject
    lateinit var allQuestionsViewModel: AllQuestionsViewModel

    lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAllQuestionBinding>(inflater, R.layout.fragment_all_question, container, false)
        binding.viewModel = allQuestionsViewModel
        val view = binding.root

        questionsRecyclerAdapter = questionsRecyclerAdapterFactory.create(View.OnClickListener {

        })

        view.question_recyclerview.adapter = questionsRecyclerAdapter
        view.question_recyclerview.layoutManager = LinearLayoutManager(context)

        view.fab.setOnClickListener {
            startActivity(Intent(context, AddQuestionActivity::class.java))
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        allQuestionsViewModel.loadQuestions()
        allQuestionsViewModel.loadCategories()
    }

    override fun onDestroy() {
        super.onDestroy()
        allQuestionsViewModel.onDestroy()
    }

    companion object {
        fun newInstance(): AllQuestionsFragment = AllQuestionsFragment()
    }

}