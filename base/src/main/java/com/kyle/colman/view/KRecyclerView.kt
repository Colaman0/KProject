package com.kyle.colaman.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.adapter.OnLoadMoreListener

/**
 * Author   : kyle
 * Date     : 2020/5/9
 * Function : 封装功能的recyclerview
 */
class KRecyclerView : RecyclerView, RefreshCallback {

    private var loadmoreActions = mutableListOf<OnLoadMoreListener>()
    private var refreshActions = mutableListOf<RefreshCallback>(this)
    private var refreshView: RefreshView? = null

    var isLoadmoreing = false

    val isRefreshing
        get() = refreshView?.isRefreshing() == true

    private var firstInitCallback: (() -> Unit)? = null

    private var KAdapter: KAdapter? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attributeSet,
            defStyleAttr
    )

    /**
     * 初始化
     *
     * @param adapter
     * @param layoutManager
     */
    fun init(adapter: KAdapter, layoutManager: RecyclerView.LayoutManager) {
        KAdapter = adapter
        setAdapter(adapter)
        adapter.bindRecyclerView(this)
        this.layoutManager = layoutManager
    }

    fun setRefreshView(view: RefreshView) {
        this.refreshView = view
        this.refreshView?.addRefreshFun(this)
    }

    fun startRefreshView() {
        if (!isRefreshing && !isLoadmoreing) {
            this.refreshView?.startRefresh()
        }
    }

    fun stopRefreshView() {
        if (isRefreshing) {
            this.refreshView?.stopRefresh()
        }
    }

    fun addRefreshListener(listener: RefreshCallback) {
        refreshView?.addRefreshFun(listener)
    }

    fun addLoadmoreListener(loadmoreCallback: OnLoadMoreListener): KRecyclerView {
        KAdapter?.addLoadmoreListener(loadmoreCallback)
        return this
    }

    /**
     * 移除loadmore监听
     *
     * @param loadmoreCallback [LoadmoreCallback]
     */
    fun removeLoadmoreListener(loadmoreCallback: OnLoadMoreListener) {
        KAdapter?.removeLoadmoreListener(loadmoreCallback)
    }

    /**
     * 判断能不能滑动，根据[layoutManager]的方向来决定
     *
     * @param layoutManager
     */
    fun canScroll(layoutManager: LinearLayoutManager) = if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
        layoutManager.canScrollVertically()
    } else {
        layoutManager.canScrollHorizontally()
    }

    fun disableLoadmore(disable: Boolean) {
        if (adapter is KAdapter) {
            (adapter as KAdapter).disableLoadmore(disable)
        }
    }

    fun finishLoadmore() {
        isLoadmoreing = false
        refreshView?.disableRefresh(true)
    }

    override fun refresh() {
    }

    override fun stopRefresh() {

    }
}


interface LoadmoreCallback {

    fun startLoadMore()

    fun endLoadMore()

}

interface RefreshCallback {
    fun refresh()

    fun stopRefresh()
}

interface RefreshView {

    fun addRefreshFun(callback: RefreshCallback)

    fun stopRefresh()

    fun startRefresh()

    fun isRefreshing(): Boolean

    fun clearFuns()

    fun disableRefresh(disable: Boolean)
}