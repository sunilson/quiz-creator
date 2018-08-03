package com.sunilson.quizcreator.presentation.MainActivity.fragments.CategoriesFragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.category_list_item.view.*
import javax.inject.Inject

class CategoriesRecyclerAdapter(
        context: Context,
        val onDeleteCallback: (Category) -> Unit,
        val editCategory: (Category) -> Unit,
        recyclerView: RecyclerView) : BaseRecyclerAdapter<Category>(context, recyclerView) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.category_list_item,
                p0,
                false
        )
        val holder = ViewHolder(binding)

        binding.root.category_delete.setOnClickListener {
            onDeleteCallback(data[holder.adapterPosition])
        }

        binding.root.category_edit.setOnClickListener {
            editCategory(data[holder.adapterPosition])
        }

        return holder
    }

}

@FragmentScope
class CategoriesRecyclerAdapterFactory @Inject constructor(private val context: Context) {
    fun create(onDeleteCallback: (Category) -> Unit, editCategory: (Category) -> Unit, recyclerView: RecyclerView): CategoriesRecyclerAdapter {
        return CategoriesRecyclerAdapter(context, onDeleteCallback, editCategory, recyclerView)
    }
}