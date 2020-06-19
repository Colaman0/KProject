package com.kyle.colaman.activity

import androidx.activity.viewModels
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivitySearchBinding
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colaman.viewmodel.SearchViewModel
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
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

    override fun initView() {
        binding?.activity = this
        recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = adapter
    }

    fun actionSearch(text: String) {
        LogUtils.d("搜索 $text")
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            if (text.isEmpty()) {
                adapter.submitItem(PagingData.empty())
            } else {
                searchViewmodel.querySearch(text).collect {
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