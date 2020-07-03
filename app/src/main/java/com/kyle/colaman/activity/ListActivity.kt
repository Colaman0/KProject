package com.kyle.colaman.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyle.colaman.R
import com.kyle.colaman.entity.Constants
import com.kyle.colaman.entity.ListActivityConfig
import com.kyle.colaman.setErrorMsg
import com.kyle.colaman.source.CollectSource
import com.kyle.colaman.viewmodel.ItemCollectViewmodel
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.network.KError
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.StatusLayout
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
            pagingSourceFactory = { listConfig.source as PagingSource<Any, Any> }).flow
    }

    val loadmoreAdapter by lazy {
        LoadMoreAdapter {
            adapter.retry()
        }
    }
    val viewmodel by viewModels<ViewModel>()


    override fun initView() {
        initToolbar()
        initRecyclerView()
        initStatusLayout()
        lifecycleScope.launch(Dispatchers.IO) {
            pager.collectLatest {
                adapter.submitItem(it.map {
                    listConfig.uiTrans.invoke(it, adapter, this@ListActivity)
                })
            }
        }
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
        swipe_refreshlayout.bindPagingAdapter(adapter)
    }

    fun initStatusLayout() {
        status_layout?.setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status === StatusLayout.STATUS_ERROR) {
                    adapter.retry()
                }
            }
        })

        status_layout.switchLayout(StatusLayout.STATUS_LOADING)
        status_layout.bindPaingState(adapter) {
            status_layout.setErrorMsg((it as KError).kTips)
        }
    }
}