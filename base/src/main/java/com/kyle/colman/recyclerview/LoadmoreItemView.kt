package com.kyle.colman.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.R
import com.kyle.colman.databinding.ItemLoadmoreBinding
import com.kyle.colman.databinding.LayoutLoadmoreBinding

/**
 * Author   : kyle
 * Date     : 2020/6/16
 * Function : 底部刷新
 */
// 这部分是一个底部loadmore的item，包含一个进度条以及重试按钮
class LoadmoreView(
    parent: ViewGroup,
    loadState: LoadState,
    val retrycallback: () -> Unit
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)!!.inflate(R.layout.layout_loadmore, parent, false)
    ) {

    val loading = ObservableBoolean()


    // 通过databinding来控制UI，具体可以查看demo的xml
    init {
        DataBindingUtil.bind<LayoutLoadmoreBinding>(itemView)?.itemview = this
        bindState(loadState)
    }

    fun bindState(loadState: LoadState) {
        loading.set(loadState is LoadState.Loading)
    }
}
