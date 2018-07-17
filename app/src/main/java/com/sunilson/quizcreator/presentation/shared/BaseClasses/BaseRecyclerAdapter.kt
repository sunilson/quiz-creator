package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.android.databinding.library.baseAdapters.BR

abstract class BaseRecyclerAdapter<T : AdapterElement>(protected val context: Context) : RecyclerView.Adapter<BaseRecyclerAdapter<T>.ViewHolder>() {

    val data: MutableList<T> = mutableListOf()

    open fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    open fun add(element: T) {
        data.add(element)
        notifyItemInserted(data.indexOf(element))
    }

    fun addAll(elements: List<T>){
        this.data.clear()
        this.data.addAll(elements)
        this.data.forEachIndexed { index, t ->
            notifyItemInserted(index)
        }
        notifyDataSetChanged()
    }

    open fun remove(element: AdapterElement) {
        val iterator = data.listIterator()
        for ((index, value) in iterator.withIndex()) {
            if (value.compareByString == element.compareByString) {
                iterator.remove()
                notifyItemRemoved(index)
            }
        }
    }

    open fun update(element: AdapterElement) {
        val iterator = data.listIterator()
        for ((_, value) in iterator.withIndex()) {
            if (value.compareByString == element.compareByString) {
                iterator.set(element as T)
                //notifyItemChanged(index)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    open inner class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: T) {
            binding.setVariable(BR.obj, obj)
            binding.executePendingBindings()
        }
    }
}

interface AdapterElement  {
    val compareByString : String
}