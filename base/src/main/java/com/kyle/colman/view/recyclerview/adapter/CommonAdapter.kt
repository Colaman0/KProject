package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder
import com.kyle.colman.view.recyclerview.CommonDiffCallBack

/**
 * Author   : kyle
 * Date     : 2020/7/13
 * Function : 通用adapter
 */
class CommonAdapter(val context: Context) : RecyclerView.Adapter<PagingVHolder>() {
    val items  = mutableListOf<PagingItemView<*,*>>()
    val oldItems  = mutableListOf<PagingItemView<*,*>>()

   val diffUtilCallback = object : DiffUtil.ItemCallback<PagingItemView<Any, *>>() {
        override fun areItemsTheSame(
            oldItem: PagingItemView<Any, *>,
            newItem: PagingItemView<Any, *>
        ): Boolean {
            return oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(
            oldItem: PagingItemView<Any, *>,
            newItem: PagingItemView<Any, *>
        ): Boolean {
            return oldItem.areContentsTheSame(newItem)
        }
    }

    val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingVHolder {
        return PagingVHolder(layoutInflater.inflate(viewType, parent, false))
    }


    fun getItem(position:Int):PagingItemView<*,*>{
        return oldItems[position]
    }

    override fun getItemCount(): Int {
        return oldItems.size
    }

    override fun onBindViewHolder(holder: PagingVHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            getItem(position).bindView(holder = holder as PagingVHolder, position = position)
        } }

    fun diffNotify(){
        val result = DiffUtil.calculateDiff(CommonDiffCallBack(oldItems,items), true)
        result.dispatchUpdatesTo(this)
        oldItems.clear()
        oldItems.addAll(items)
    }

    fun bindRecyclerView(recyclerview:RecyclerView) {

    }
}