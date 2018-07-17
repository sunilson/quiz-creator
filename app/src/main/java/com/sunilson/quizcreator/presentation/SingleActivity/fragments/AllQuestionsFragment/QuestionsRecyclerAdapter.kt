package com.sunilson.quizcreator.presentation.SingleActivity.fragments.AllQuestionsFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.Application.di.FragmentScope
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import javax.inject.Inject

class QuestionsRecyclerAdapter(context: Context, onDeleteClickListener: View.OnClickListener) : BaseRecyclerAdapter<Question>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.question_list_item,
                parent,
                false
        )

        binding.root.setOnClickListener {

        }
        //binding.root.setOnLongClickListener(onLongClickListener)

        return ViewHolder(binding)
    }
}

@FragmentScope
class QuestionsRecyclerAdapterFactory @Inject constructor(private val context: Context) {
    fun create(onDeleteClickListener: View.OnClickListener): QuestionsRecyclerAdapter {
        return QuestionsRecyclerAdapter(context, onDeleteClickListener)
    }
}