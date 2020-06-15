package com.kyle.colaman.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.viewmodel.ItemArticle
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.tencent.smtt.sdk.b.a.i
import kotlinx.android.synthetic.main.activity_page.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PageActivity : AppCompatActivity() {
    val adapter by lazy {
        PagingAdapter(this)
    }

    val source by lazy {
        MainSource()
    }

    var job: Job? = null

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)
        recyclerview.adapter = adapter.withLoadStateFooter(StateAdapter())
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.addDataRefreshListener { swipeRefreshLayout.isRefreshing = false }
        swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        lifecycleScope.launch {
            source.pager.collect {
                if (isActive) {
                    LogUtils.d("刷新数据")
                    adapter.submitData(it.map { ItemArticle(it) as PagingItemView<Any> })
                }
            }
        }

        adapter.addLoadStateListener { LogUtils.d(it.refresh) }

    }
}