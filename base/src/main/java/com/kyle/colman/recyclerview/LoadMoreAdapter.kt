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
    LoadStateAdapter<LoadmoreView>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadmoreView {
        return LoadmoreView(parent, loadState, retrycallback)
    }

    override fun onBindViewHolder(holder: LoadmoreView, loadState: LoadState) {
        holder.bindState(loadState)
    }

}
