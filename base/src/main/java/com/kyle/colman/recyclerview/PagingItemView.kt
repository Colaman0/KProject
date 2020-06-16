package com.kyle.colman.recyclerview

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

    open fun onBindView(holder: PagingVHolder, position: Int) {
        this.holder = holder
        binding = DataBindingUtil.bind(holder.itemView)
    }

    abstract fun areItemsTheSame(data: T): Boolean

    abstract fun areContentsTheSame(data: T): Boolean
}