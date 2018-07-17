package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes

abstract class BaseSpinnerArrayAdapter<T>(context: Context, @LayoutRes resource: Int) : ArrayAdapter<T>(context, resource) {

    val data: MutableList<T> = mutableListOf()

    override fun getCount(): Int = data.size
    override fun getItem(position: Int): T = data[position]

    fun getViewHolder(layout: Int, convertView: View?, parent: ViewGroup?): Pair<ViewHolder, View?> {
        var row = convertView

        val viewHolder = if (row == null) {
            row = LayoutInflater.from(context).inflate(layout, parent, false)
            val tempViewholder = ViewHolder(row.findViewById(R.id.text1))
            row.tag = tempViewholder
            tempViewholder
        } else {
            row.tag as ViewHolder
        }

        return Pair(viewHolder, row)
    }

    class ViewHolder(val textView: TextView)

}