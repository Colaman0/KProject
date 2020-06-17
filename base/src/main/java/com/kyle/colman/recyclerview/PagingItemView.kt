package com.kyle.colman.recyclerview

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Author   : kyle
 * Date     : 2020/6/15
 * Function : item view
 */

abstract class PagingItemView<T : Any, B : ViewDataBinding>(@LayoutRes val layoutRes: Int) {
    lateinit var holder: PagingVHolder
    var binding: B? = null
    var context: Context? = null

    open fun bindView(holder: PagingVHolder, position: Int) {
        context = holder.itemView.context
        this.holder = holder
        binding = DataBindingUtil.bind(holder.itemView)
        holder.itemView.setOnClickListener { onItemClick() }
        onBindView(holder, position)
    }

    abstract fun onBindView(
        holder: PagingVHolder,
        position: Int
    )


    open fun onItemClick() {}

    abstract fun areItemsTheSame(data: T): Boolean

    abstract fun areContentsTheSame(data: T): Boolean
}