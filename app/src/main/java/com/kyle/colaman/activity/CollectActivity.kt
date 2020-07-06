package com.kyle.colaman.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.setErrorMsg
import com.kyle.colaman.source.CollectSource
import com.kyle.colaman.viewmodel.ItemCollectViewmodel
import com.kyle.colman.helper.*
import com.kyle.colman.network.KError
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.StatusLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class CollectActivity : KActivity<Nothing>(R.layout.activity_collect) {
    val adapter by lazy {
        PagingAdapter(context)
    }
    val pager by lazy {
        Pager(
            PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { CollectSource() }).flow
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
//            pager.collect {
//                adapter.submitItem(it.map {
//                    ItemCollectViewmodel(
//                        it, this@CollectActivity, {
//                            adapter.notifyItemRemoved(it)
//                        }
//                    )
//                })
//            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
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