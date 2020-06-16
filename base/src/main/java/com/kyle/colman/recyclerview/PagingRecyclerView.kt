package com.kyle.colman.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.colaman.statuslayout.StatusLayout
import com.kyle.colman.R
import kotlinx.android.synthetic.main.layout_paging_recyclerview.view.*

/**
 * Author   : kyle
 * Date     : 2020/6/17
 * Function : 分页recyclerview
 */
@OptIn(ExperimentalPagingApi::class)

class PagingRecyclerView : ConstraintLayout {
    val recyclerView
        get() = recyclerview

    val refreshLayout
        get() = swipe_refreshlayout

    val statusLayout
        get() = status_layout

    val pagingAdapter by lazy {
        PagingAdapter(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_paging_recyclerview, this, true)
        initView()
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)


    fun initView() {
        recyclerView.adapter = pagingAdapter

        swipe_refreshlayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }

        pagingAdapter.apply {
            addDataRefreshListener {
                refreshLayout.isRefreshing = false
            }
            addLoadStateListener { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> refreshLayout.isRefreshing = false
                    is LoadState.NotLoading -> refreshLayout.isRefreshing = false
                }
            }
            withLoadStateFooter(LoadMoreAdapter {
                this.retry()
            })
        }
    }
}