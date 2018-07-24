package com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_question_choose_type.view.*

class AddQuestionTypeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_question_choose_type, container, false)

        view.question_type_multiple_button.setOnClickListener {
            (activity as AddQuestionActivity).startCreation(QuestionType.MULTIPLE_CHOICE)
        }

        view.question_type_single_button.setOnClickListener {
            (activity as AddQuestionActivity).startCreation(QuestionType.SINGLE_CHOICE)
        }

        return view
    }

    companion object {
        fun newInstance(): AddQuestionTypeFragment {
            return AddQuestionTypeFragment()
        }
    }
}