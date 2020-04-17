package com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sunilson.quizcreator.R

import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.all_questions_list_item.view.*


class QuestionsRecyclerAdapter(
    context: Context,
    private val onDeleteCallback: (Question) -> Unit,
    val saveQuestionCallback: (Question) -> Unit,
    private val editQuestionCallback: (Question) -> Unit,
    recyclerView: RecyclerView
) : BaseRecyclerAdapter<Question>(context, recyclerView) {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.all_questions_list_item,
            parent,
            false
        )

        val holder = ViewHolder(binding)

        binding.root.question_list_item.setOnClickListener {
            editQuestionCallback(data[holder.adapterPosition])
        }

        binding.root.question_list_item_answers_delete.setOnClickListener {
            onDeleteCallback(data[holder.adapterPosition])
        }

        binding.root.question_list_item_edit.setOnClickListener {
            editQuestionCallback(data[holder.adapterPosition])
        }

        return holder
    }
}


class QuestionsRecyclerAdapterFactory (private val context: Context) {
    fun create(
        onDeleteCallback: (Question) -> Unit,
        saveQuestionCallback: (Question) -> Unit,
        editQuestionCallback: (Question) -> Unit,
        recyclerView: RecyclerView
    ): QuestionsRecyclerAdapter {
        return QuestionsRecyclerAdapter(
            context,
            onDeleteCallback,
            saveQuestionCallback,
            editQuestionCallback,
            recyclerView
        )
    }
}