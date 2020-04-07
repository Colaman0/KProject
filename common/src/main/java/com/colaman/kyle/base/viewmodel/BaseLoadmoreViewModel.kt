package com.colaman.kyle.base.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.colaman.kyle.common.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.common.recyclerview.adapter.FeaturesRecyclerViewAdapter
import com.colaman.kyle.common.recyclerview.adapter.LOADMORE_STATE

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/1
 *     desc   : loadmoreitem的基类，封装一些方法
 * </pre>
 */
abstract class BaseLoadmoreViewModel<B : ViewDataBinding, VM : Any>() : RecyclerItemViewModel<B, VM>() {
    protected var adapter: FeaturesRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null

    private var reverseLayout = false
    private var orientationFlag = RecyclerView.VERTICAL
    private val loadmoreCallBacks = mutableListOf<() -> Unit>()

    /**
     * loadmore当前的状态
     */
    var state: LOADMORE_STATE = LOADMORE_STATE.SUCCESS
        set(value) {
            if (value != field) {
                onStateChange(value)
            }
            field = value
        }


    /**
     * loadmore状态改变
     * @param state LOADMORE_STATE
     */
    abstract fun onStateChange(state: LOADMORE_STATE)

    /**
     * 绑定adapter
     *
     * @param adapter
     */
    fun bindAdapter(adapter: FeaturesRecyclerViewAdapter) {
        this@BaseLoadmoreViewModel.adapter = adapter
        recyclerView = adapter.recyclerView
        var layoutManager = recyclerView?.layoutManager
        // 判断recyclerview的方向
        orientationFlag =
            if (layoutManager?.canScrollVertically() == true)
                RecyclerView.VERTICAL
            else
                RecyclerView.HORIZONTAL
        // 判断是否翻转过来
        if (layoutManager is LinearLayoutManager) {
            reverseLayout = layoutManager.reverseLayout
        } else if (layoutManager is StaggeredGridLayoutManager) {
            reverseLayout = layoutManager.reverseLayout
        }

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isAttached) {
                    showLoadmore()
                }
            }
        })
    }

    fun showLoadmore() {
        if (state != LOADMORE_STATE.LOADING) {
            state = LOADMORE_STATE.LOADING
            loadmoreCallBacks.forEach {
                it.invoke()
            }
        }
    }

    fun addLoadMoreListener(callback: () -> Unit) {
        loadmoreCallBacks.add(callback)
    }
}