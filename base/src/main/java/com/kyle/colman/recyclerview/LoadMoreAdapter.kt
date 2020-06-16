package com.kyle.colman.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Author   : kyle
 * Date     : 2020/6/16
 * Function : load more adapter 添加到paging的adapter中
 */
class LoadMoreAdapter(private val retrycallback: () -> Unit) :
    LoadStateAdapter<RecyclerView.ViewHolder>() {
    val loadmoreItem by lazy {
        LoadmoreItemView(retrycallback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        loadmoreItem.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        return PagingVHolder(
            LayoutInflater.from(parent.context).inflate(loadmoreItem.layoutRes, parent, false)
        )
    }

}
