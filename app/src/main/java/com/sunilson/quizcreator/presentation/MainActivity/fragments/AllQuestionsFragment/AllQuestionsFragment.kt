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
import com.sunilson.quizcreator.presentation.shared.ADD_QUESTIONS_INTENT
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
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

        questionsRecyclerAdapter = questionsRecyclerAdapterFactory.create({
            disposable.add(allQuestionsViewModel.deleteQuestion(it).subscribe({
                questionsRecyclerAdapter.remove(it)
            }, {
                context?.showToast(it.message)
            }))
        }, {
            disposable.add(allQuestionsViewModel.updateQuestion(it).subscribe({
                questionsRecyclerAdapter.update(it)
            }, {
                context?.showToast(it.message)
            }))
        }, recyclerView = view.question_recyclerview)

        view.question_recyclerview.adapter = questionsRecyclerAdapter
        view.question_recyclerview.isNestedScrollingEnabled = true
        view.question_recyclerview.setHasFixedSize(true)
        view.question_recyclerview.layoutManager = LinearLayoutManager(context)

        view.fab.setOnClickListener {
            startActivityForResult(Intent(context, AddQuestionActivity::class.java), ADD_QUESTIONS_INTENT)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADD_QUESTIONS_INTENT -> {
                allQuestionsViewModel.loadQuestions()
            }
        }
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