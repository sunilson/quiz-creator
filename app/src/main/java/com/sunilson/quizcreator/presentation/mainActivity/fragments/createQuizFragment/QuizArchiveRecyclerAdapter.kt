package com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunilson.quizcreator.application.di.scopes.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseRecyclerAdapter
import javax.inject.Inject

class QuizArchiveRecyclerAdapter constructor(val onClickCallback: (Quiz) -> kotlin.Unit, context: Context, recyclerView: RecyclerView) : BaseRecyclerAdapter<Quiz>(context, recyclerView) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.quiz_archive_item,
                p0,
                false
        )
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener {
            onClickCallback(data[holder.adapterPosition])
        }
        return holder
    }
}

@FragmentScope
class QuizArchiveRecyclerAdapterFactory @Inject constructor(val context: Context) {
    fun create(onClickCallback: (Quiz) -> Unit, recyclerView: RecyclerView): QuizArchiveRecyclerAdapter {
        return QuizArchiveRecyclerAdapter(onClickCallback, context, recyclerView)
    }
}