package com.sunilson.quizcreator.presentation.shared

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseSpinnerArrayAdapter
import javax.inject.Inject

@FragmentScope
class CategorySpinnerAdapter @Inject constructor(context: Context) : BaseSpinnerArrayAdapter<Category>(context, android.R.layout.simple_list_item_1) {


    fun setCategories(cateogries: List<Category>) {
        data.clear()
        data.addAll(cateogries)
        notifyDataSetChanged()
    }

    fun addCategory(category: Category) {
        data.add(category)
        notifyDataSetChanged()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val res = getViewHolder(android.R.layout.simple_list_item_1, convertView, parent)
        res.first.textView.text = data[position].name
        return res.second!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val res = getViewHolder(android.R.layout.simple_spinner_item, convertView, parent)
        res.first.textView.text = data[position].name
        res.second!!.setPadding(0, res.second!!.paddingTop, res.second!!.paddingRight, res.second!!.paddingBottom)
        return res.second!!
    }

}