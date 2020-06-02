package com.kyle.colman.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.helper.kHandler
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.adapter.OnLoadMoreListener
import kotlinx.coroutines.*

/**
 * Author   : kyle
 * Date     : 2020/5/9
 * Function : 封装功能的recyclerview
 */
class KRecyclerView : RecyclerView, RefreshCallback, OnLoadMoreListener {

    private var refreshView: RefreshView? = null
    var dataCreator: IRVDataCreator<Any>? = null
    var isLoadmoreing = false
    var pageDTO: IPageDTO<*>? = null
    var lifecycleCoroutineScope: LifecycleCoroutineScope? = null

    val isRefreshing
        get() = refreshView?.isRefreshing() == true

    var currentJob: CompletableJob? = null


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
        adapter.addLoadmoreListener(this)
    }

    fun setRefreshView(view: RefreshView) {
        this.refreshView = view
        this.refreshView?.addRefreshFun(this)
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
    fun canScroll(layoutManager: LinearLayoutManager) =
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            layoutManager.canScrollVertically()
        } else {
            layoutManager.canScrollHorizontally()
        }

    fun startLoadmore() {
        if (!isLoadmoreing) {
            currentJob?.cancel()
            isLoadmoreing = true
            currentJob = SupervisorJob()
            loadDataByPage(pageDTO?.currentPage() ?: 0 + 1, currentJob!!)
            (adapter as KAdapter).disableLoadmore(true)
        }
    }

    fun endLoadmore() {
        currentJob?.cancel()
        KAdapter?.disableLoadmore(false)
        isLoadmoreing = false
    }

    fun startRefresh() {
        if (isLoadmoreing) {
            endLoadmore()
        }
        refreshView?.startRefresh()
    }

    fun endRefresh() {
        currentJob?.cancel()
        this.refreshView?.stopRefresh()
    }

    override fun refresh() {
        currentJob?.cancel()
        currentJob = SupervisorJob()
        loadDataByPage(1, currentJob!!)
    }

    override fun onLoadMore() {
        startLoadmore()
    }

    override fun stopRefresh() {
    }

    private fun loadDataByPage(page: Int, job: CompletableJob) {
        if (dataCreator != null) {
            if (lifecycleCoroutineScope != null) {
                lifecycleCoroutineScope!!.launch(Dispatchers.IO + job + kHandler {
                    if (isRefreshing) endRefresh() else endLoadmore()
                }) {
                    val data = dataCreator?.loadDataByPage(page = page) ?: return@launch
                    if (isActive) {
                        updateAdapterData(data)
                    } else {
                        endRefresh()
                        endLoadmore()
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO + job + kHandler {
                    if (isRefreshing) endRefresh() else endLoadmore()
                }).launch {
                    val data = dataCreator?.loadDataByPage(page = page) ?: return@launch
                    if (isActive) {
                        updateAdapterData(data)
                    } else {
                        endRefresh()
                        endLoadmore()
                    }
                }
            }
        }
    }

    suspend fun updateAdapterData(data: IPageDTO<Any>) {
        endRefresh()
        endLoadmore()
        if (data.isFirstPage()) {
            KAdapter?.clear()
        }
        pageDTO = data
        KAdapter?.addAll(data.pageData().map { dataCreator!!.dataToItemView(it) })
        KAdapter?.diffNotifydatasetchanged(data.isLastPage())
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