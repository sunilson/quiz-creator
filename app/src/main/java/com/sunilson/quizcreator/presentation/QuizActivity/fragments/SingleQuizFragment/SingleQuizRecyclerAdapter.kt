package com.sunilson.quizcreator.presentation.QuizActivity.fragments.SingleQuizFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import javax.inject.Inject

@FragmentScope
class SingleQuizRecyclerAdapter @Inject constructor(context: Context) : BaseRecyclerAdapter<Question>(context) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.single_quiz_question_list_item,
                p0,
                false
        )
        val holder = ViewHolder(binding)
        return holder
    }
}