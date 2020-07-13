package com.kyle.colaman.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyle.colaman.R
import com.kyle.colaman.entity.CollectEntity
import com.kyle.colaman.entity.Constants
import com.kyle.colaman.entity.ListActivityConfig
import com.kyle.colaman.setErrorMsg
import com.kyle.colaman.viewmodel.ListViewmodel
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.network.KError
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.StatusLayout
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class ListActivity : KActivity<Nothing>(R.layout.activity_list) {
    val listConfig by lazy {
        intent.getSerializableExtra(Constants.data) as ListActivityConfig<Any>
    }

    val adapter by lazy {
        PagingAdapter(context)
    }
    val pager by lazy {
        Pager(
            PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { (listConfig.source.invoke() as PagingSource<Any, Any>) }).flow
    }

    val loadmoreAdapter by lazy {
        LoadMoreAdapter {
            adapter.retry()
        }
    }
    val viewmodel by viewModels<ListViewmodel>()

    val pagingItems = mutableListOf<PagingItemView<*, *>>()

    private var _removedItemsFlow = MutableStateFlow(mutableListOf<Int>())
    private val removedItemsFlow: Flow<MutableList<Int>> get() = _removedItemsFlow


    override fun initView() {
        initToolbar()
        initRecyclerView()
        initStatusLayout()
        lifecycleScope.launch(Dispatchers.IO) {
            pager
                .cachedIn(viewmodel.viewModelScope)
                .combine(removedItemsFlow) { pagingData, removed ->
                    pagingData.filter {
                        (it as CollectEntity).id !in removed
                    }
                }
                .cachedIn(viewmodel.viewModelScope)
                .collectLatest {
                    adapter.submitItem(it.map {
                        listConfig.uiTrans.invoke(
                            it,
                            adapter,
                            this@ListActivity
                        )
                    })
                }
        }
    }

    fun remove(item: MutableList<Int>) {
        val removes = _removedItemsFlow.value
        item.addAll(removes)
        _removedItemsFlow.value = item
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = listConfig.title
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = this.adapter.withLoadStateFooter(loadmoreAdapter)
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                loadmoreAdapter.loadState = LoadState.NotLoading(endOfPaginationReached = true)
            }
        }
        swipe_refreshlayout.bindPagingAdapter(adapter, pagingItems)
    }

    fun initStatusLayout() {
        status_layout?.setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status === StatusLayout.STATUS_ERROR) {
                    adapter.retry()
                } else if (status === StatusLayout.STATUS_EMPTY) {
                    adapter.refresh()
                    status_layout.switchLayout(StatusLayout.STATUS_LOADING)
                }
            }
        })

        status_layout.switchLayout(StatusLayout.STATUS_LOADING)
        status_layout.bindPaingState(adapter) {
            status_layout.setErrorMsg((it as KError).kTips)
        }
    }
}