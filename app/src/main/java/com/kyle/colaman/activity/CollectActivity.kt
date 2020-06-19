package com.kyle.colaman.activity

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.colaman.statuslayout.StatusLayout
import com.kyle.colaman.R
import com.kyle.colaman.source.CollectSource
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_collect.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CollectActivity : KActivity<Nothing>(R.layout.activity_collect) {
    val adapter by lazy {
        PagingAdapter(context)
    }
    val pager by lazy {
        Pager(
            PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { CollectSource() }).flow
    }
    val viewmodel by viewModels<ViewModel>()

    @OptIn(ExperimentalPagingApi::class)
    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = this.adapter
        status_layout.switchLayout(StatusLayout.STATUS_LOADING)
        status_layout.bindPaingState(adapter) {
            adapter.retry()
        }
        swipe_refreshlayout.bindPagingAdapter(adapter)
        lifecycleScope.launch {
            pager.collect {
                adapter.submitItem(it.map {
                    ItemArticleViewModel(
                        it,
                        lifecycleOwner = this@CollectActivity
                    )
                })
            }
        }
    }
}