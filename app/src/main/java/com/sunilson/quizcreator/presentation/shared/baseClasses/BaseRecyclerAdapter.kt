package com.sunilson.quizcreator.presentation.shared.baseClasses

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sunilson.quizcreator.BR

abstract class BaseRecyclerAdapter<T : AdapterElement>(protected val context: Context, private val recyclerView: RecyclerView? = null) : RecyclerView.Adapter<BaseRecyclerAdapter<T>.ViewHolder>() {

    val data: MutableList<T> = mutableListOf()

    open fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    open fun add(element: T) {
        data.add(element)
        notifyItemInserted(data.indexOf(element))
    }

    open fun addAll(elements: List<T>) {
        val size = this.data.size
        this.data.clear()
        recyclerView?.recycledViewPool?.clear()
        recyclerView?.setRecycledViewPool(RecyclerView.RecycledViewPool())
        notifyItemRangeRemoved(0, size)
        this.data.addAll(elements)
        notifyItemRangeInserted(0, data.size)

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
        for ((i, value) in iterator.withIndex()) {
            if (value.compareByString == element.compareByString) {
                iterator.set(element as T)
                notifyItemChanged(i)
                //notifyDataSetChanged()
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

interface AdapterElement {
    val compareByString: String
}