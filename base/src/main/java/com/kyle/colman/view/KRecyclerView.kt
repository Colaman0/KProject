package com.kyle.colman.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.helper.copy
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
    var loadJob: Job? = null
    var isLoadmoreing = false
    var pageDTO: IPageDTO<Any>? = null
    val dataCreatorLiveData: MutableLiveData<IPageDTO<Any>> = MutableLiveData()
    lateinit var lifecycleOwner: LifecycleOwner

    val isRefreshing
        get() = refreshView?.isRefreshing() == true

    lateinit var adapter: KAdapter

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
        this.adapter = adapter
        this.lifecycleOwner = lifecycleOwner
        this.layoutManager = layoutManager
        setAdapter(adapter)
        adapter.bindRecyclerView(this)
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
        adapter?.addLoadmoreListener(loadmoreCallback)
        return this
    }

    /**
     * 移除loadmore监听
     *
     * @param loadmoreCallback [LoadmoreCallback]
     */
    fun removeLoadmoreListener(loadmoreCallback: OnLoadMoreListener) {
        adapter?.removeLoadmoreListener(loadmoreCallback)
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
            adapter.disableLoadmore(true)
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
        adapter.disableLoadmore(false)
        loadDataByPage(pageDTO?.firstPageNum() ?: 0)
    }


    fun endLoadmore() {
        if (isLoadmoreing) {
            adapter.disableLoadmore(false)
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

    /**
     * 加载对应页数的数据
     *
     * @param page
     */
    private fun loadDataByPage(page: Int) {
        if (dataCreator != null) {
            // 把原本的page请求取消掉，避免多次refresh之后刷新了多次adapter
            loadJob?.cancel()
            loadJob = lifecycleOwner.lifecycleScope.launch(kHandler {
                if (isRefreshing) {
                    endRefresh()
                }
                if (isLoadmoreing) {
                    endLoadmore()
                }
            }) {
                if (isActive) {
                    val datas = dataCreator!!.loadDataByPage(page)
                    dataCreatorLiveData.postValue(datas)
                }
            }
        }
    }

    private suspend fun updateAdapterData(data: IPageDTO<Any>) {
        endRefresh()
        isLoadmoreing = false
        refreshView?.disableRefresh(true)
        val newItems = adapter.getDatas().copy()
        if (data.isFirstPage()) {
            newItems.clear()
        }
        pageDTO = data
        newItems.addAll(data.pageData().map { dataCreator!!.dataToItemView(it) })
        adapter.diffNotifydatasetchanged(!data.isLastPage(), newItems)
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