package com.kyle.colman.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.helper.kHandler
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.adapter.OnLoadMoreListener
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Author   : kyle
 * Date     : 2020/5/9
 * Function : 封装功能的recyclerview
 */
class KRecyclerView : RecyclerView, RefreshCallback, OnLoadMoreListener {

    private var refreshView: RefreshView? = null
    var dataCreator: IRVDataCreator<Any>? = null
    var isLoadmoreing = false
    var pageDTO: IPageDTO<Any>? = null
    val dataCreatorLiveData: MutableLiveData<IPageDTO<Any>> = MutableLiveData()
    lateinit var lifecycleOwner: LifecycleOwner

    val isRefreshing
        get() = refreshView?.isRefreshing() == true

    private var KAdapter: KAdapter? = null

    private val dataObserver = Observer<IPageDTO<Any>> {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) { updateAdapterData(it) }
    }

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
    fun init(
        adapter: KAdapter,
        layoutManager: RecyclerView.LayoutManager,
        lifecycleOwner: LifecycleOwner
    ) {
        KAdapter = adapter
        this.lifecycleOwner = lifecycleOwner
        setAdapter(adapter)
        adapter.bindRecyclerView(this)
        this.layoutManager = layoutManager
        adapter.addLoadmoreListener(this)
        dataCreatorLiveData.observe(lifecycleOwner, dataObserver)
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
        if (!isLoadmoreing && !isRefreshing) {
            isLoadmoreing = true
            refreshView?.disableRefresh(false)
            if (isRefreshing) {
                endRefresh()
            }
            (adapter as KAdapter).disableLoadmore(true)
            loadDataByPage((pageDTO?.currentPage() ?: 0) + 1)
        }
    }


    fun startRefresh() {
        if (!isRefreshing && !isLoadmoreing) {
            refreshView?.startRefresh()
        }
    }

    override fun refresh() {
        if (isLoadmoreing) {
            return
        }
        KAdapter?.disableLoadmore(false)
        loadDataByPage(pageDTO?.firstPageNum() ?: 0)
    }


    fun endLoadmore() {
        if (isLoadmoreing) {
            KAdapter?.disableLoadmore(false)
            isLoadmoreing = false
        }
    }

    fun endRefresh() {
        this.refreshView?.stopRefresh()
    }


    override fun onLoadMore() {
        startLoadmore()
    }

    override fun stopRefresh() {
    }

    private fun loadDataByPage(page: Int) {
        if (dataCreator != null) {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO + kHandler {
                if (isRefreshing) {
                    endRefresh()
                }
                if (isLoadmoreing) {
                    endLoadmore()
                }
            }) {
                val datas = dataCreator!!.loadDataByPage(page)
                LogUtils.d("网络请求成功")
                dataCreatorLiveData.postValue(datas)
            }
        }
    }

    private suspend fun updateAdapterData(data: IPageDTO<Any>) {
        LogUtils.d("更新UI")
        endRefresh()
        isLoadmoreing = false
        refreshView?.disableRefresh(true)
        if (data.isFirstPage()) {
            KAdapter?.clear()
        }
        pageDTO = data
        withContext(Dispatchers.Default) {
            KAdapter?.addAll(data.pageData().map { dataCreator!!.dataToItemView(it) })
        }
        if (data.isFirstPage()) {
            KAdapter?.notifyDataSetChanged()
        } else {
            KAdapter?.diffNotifydatasetchanged(!data.isLastPage())
        }
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