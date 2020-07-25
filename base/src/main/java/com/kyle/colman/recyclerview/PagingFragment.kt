package com.kyle.colman.recyclerview

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.R
import com.kyle.colman.config.PageViewConfig
import com.kyle.colman.databinding.FragmentRecyclerviewBinding
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.network.KError
import com.kyle.colman.view.KFragment
import com.kyle.colman.view.StatusLayout
import com.kyle.colman.viewmodel.RecyclerViewFragmentVModel
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/7/24
 * Function : 封装了recyclerView的Fragment
 */
@ExperimentalPagingApi
class PagingFragment<T : Any> :
    KFragment<FragmentRecyclerviewBinding>(R.layout.fragment_recyclerview) {
    lateinit var listconfig: PageViewConfig<T>

    val adapter by lazy {
        PagingAdapter(context!!)
    }
    lateinit var pagerFlow: Flow<PagingData<T>>

    val loadmoreAdapter by lazy {
        LoadMoreAdapter {
            adapter.retry()
        }
    }

    val viewmodel by viewModels<RecyclerViewFragmentVModel>()

    val pagingItems = mutableListOf<PagingItemView<*, *>>()

    /**
     * 初始分页相关的数据源数据
     *
     * @param pageConfig
     * @param source
     */
    fun initPagerConfig(listconfig: PageViewConfig<T>) {
        this.listconfig = listconfig
        pagerFlow = Pager(
            PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { (listconfig.source.invoke() as PagingSource<Int, T>) }).flow
        lifecycleScope.launch(Dispatchers.IO) {
            pagerFlow
                .cachedIn(viewmodel.viewModelScope)
                .collectLatest {
                    adapter.submitItem(it.map {
                        listconfig.uiTrans.invoke(it, adapter)
                    })
                }
        }
        adapter.refresh()
    }

    override fun initView() {
        initRecyclerView()
        initStatusLayout()
    }

    fun initRecyclerView() {
        recyclerview.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = this.adapter.withLoadStateFooter(loadmoreAdapter)
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                loadmoreAdapter.loadState = LoadState.NotLoading(endOfPaginationReached = true)
            }
        }
        swipe_refreshlayout.bindPagingAdapter(adapter, pagingItems)
    }

    fun initStatusLayout() {
        statuslayout.setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status === StatusLayout.STATUS_ERROR) {
                    adapter.retry()
                } else if (status === StatusLayout.STATUS_EMPTY) {
                    adapter.refresh()
                    statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
                }
            }
        })

        statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
        statuslayout.bindPaingState(adapter) {
            LogUtils.e(it)
        }
    }
}