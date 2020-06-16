package com.kyle.colman.recyclerview

import androidx.databinding.ObservableBoolean
import androidx.paging.LoadState
import com.kyle.colman.R
import com.kyle.colman.databinding.LayoutLoadmoreBinding

/**
 * Author   : kyle
 * Date     : 2020/6/16
 * Function : 底部刷新
 */
class LoadmoreItemView(val retrycallback: () -> Unit) :
    PagingItemView<Nothing, LayoutLoadmoreBinding>(R.layout.layout_loadmore) {

    val loading = ObservableBoolean()

    fun bindState(loadState: LoadState) {
        loading.set(loadState is LoadState.Loading)
    }

    override fun onBindView(holder: PagingVHolder, position: Int) {
        super.onBindView(holder, position)
        binding?.itemview = this
    }

    override fun areContentsTheSame(data: Nothing) = true

    override fun areItemsTheSame(data: Nothing) = true
}