package com.kyle.colaman.activity

import android.view.View
import androidx.activity.viewModels
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivitySearchBinding
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colaman.viewmodel.SearchViewModel
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.StatusLayout
import kotlinx.android.synthetic.main.activity_pocket.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.recyclerview
import kotlinx.android.synthetic.main.activity_search.status_layout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SearchActivity : KActivity<ActivitySearchBinding>(R.layout.activity_search) {
    val queryText = ObservableField("")
    val searchViewmodel by viewModels<SearchViewModel>()
    val adapter = PagingAdapter(context)
    var searchJob: Job? = null

    init {
        lifecycleScope.launch {
            callbackFlow<String> {
                val callback = object :
                    Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        queryText.get()?.let { offer(it) }
                    }
                }
                queryText.addOnPropertyChangedCallback(callback)
                awaitClose { queryText.removeOnPropertyChangedCallback(callback) }
            }.debounce(500).collectLatest { actionSearch(it) }
        }
    }

    @ExperimentalPagingApi
    override fun initView() {
        binding?.activity = this
        recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter.withLoadStateFooter(LoadMoreAdapter {
            adapter.retry()
        })
        adapter.addDataRefreshListener {
            if (it) {
                status_layout.switchLayout(StatusLayout.STATUS_EMPTY)
            } else {
                status_layout.showDefaultContent()
            }
        }
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
    }

    fun actionSearch(text: String) {
        if (text.isEmpty()) {
            return
        }
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            if (text.isEmpty()) {
                adapter.submitItem(PagingData.empty())
            } else {
                searchViewmodel.querySearch(text).collectLatest {
                    adapter.submitItem(it.map { ItemArticleViewModel(it, this@SearchActivity) })
                }
            }
        }
    }

    /**
     * 清除输入框内容
     *
     */
    fun clearQuery() {
        queryText.set("")
    }
}