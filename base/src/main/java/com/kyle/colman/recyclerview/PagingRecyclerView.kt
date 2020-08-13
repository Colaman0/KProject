package com.kyle.colman.recyclerview

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kyle.colman.databinding.LayoutPagingRecyclerviewBinding

/**
 * Author   : kyle
 * Date     : 2020/6/17
 * Function : 分页recyclerview
 */
@OptIn(ExperimentalPagingApi::class)
fun LayoutPagingRecyclerviewBinding.init(
    adapter: PagingAdapter,
    layoutManager: RecyclerView.LayoutManager
) {
    swipeRefreshlayout.setOnRefreshListener {
        adapter.refresh()
    }
    recyclerview.layoutManager = layoutManager

    adapter.apply {
        addDataRefreshListener {
            swipeRefreshlayout.isRefreshing = false
        }
        addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Error -> swipeRefreshlayout.isRefreshing = false
                is LoadState.NotLoading -> swipeRefreshlayout.isRefreshing = false
            }
        }
        withLoadStateFooter(LoadMoreAdapter {
            this.retry()
        })
    }
}