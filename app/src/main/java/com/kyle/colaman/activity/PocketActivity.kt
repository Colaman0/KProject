package com.kyle.colaman.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.kyle.colaman.R
import com.kyle.colaman.api.PocketDao
import com.kyle.colaman.helper.PocketRoom
import com.kyle.colaman.source.PocketSource
import com.kyle.colaman.viewmodel.AppViewmodel
import com.kyle.colaman.viewmodel.ItemPocketViewmodel
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.helper.dp2px
import com.kyle.colman.helper.kHandler
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.CommonDialog
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.StatusLayout
import com.kyle.colman.view.recyclerview.SimpleDecoration
import kotlinx.android.synthetic.main.activity_pocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PocketActivity() : KActivity<Nothing>(R.layout.activity_pocket) {
    val adapter by lazy {
        PagingAdapter(context)
    }
    val pager by lazy {

        Pager(
            PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { PocketSource() }).flow
    }
    val viewmodel by viewModels<ViewModel>()
    val loadingDialog by lazy {
        CommonDialog(context).apply { builder = CommonDialog.CommonDialogBuilder(text = "操作中...") }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
        status_layout.switchLayout(StatusLayout.STATUS_LOADING)


        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerview.adapter = this.adapter.withLoadStateFooter(LoadMoreAdapter {
            adapter.retry()
        })
        status_layout.switchLayout(StatusLayout.STATUS_LOADING)
        status_layout.bindPaingState(adapter) {
            status_layout.switchLayout(StatusLayout.STATUS_ERROR)
        }
        swipe_refreshlayout.bindPagingAdapter(adapter)
        lifecycleScope.launch {
            pager.collect { datas ->
                adapter.submitItem(datas.map {
                    var index = adapter.itemCount
                    ItemPocketViewmodel(it) { removeEntity, position ->
                        loadingDialog.show()
                        AppViewmodel.viewModelScope.launch(Dispatchers.IO + kHandler {
                            loadingDialog.dismiss()
                        }) {
                            PocketRoom.getDatabase(context).pocketDao()
                                .removePocketArticle(removeEntity)
                            loadingDialog.dismiss()
                            withContext(Dispatchers.Main) {
                                adapter.notifyItemRemoved(position)
                            }
                        }
                    }
                })
            }
        }

        statusLayout?.setLayoutClickListener(object : StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status === StatusLayout.STATUS_ERROR) {
                    adapter.retry()
                }
            }
        })
    }
}