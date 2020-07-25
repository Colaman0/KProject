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
import com.kyle.colman.impl.IPageSource
import com.kyle.colman.impl.PageResult
import com.kyle.colman.impl.PageResult.*
import com.kyle.colman.impl.RESULT
import com.kyle.colman.network.KError
import com.kyle.colman.view.KFragment
import com.kyle.colman.view.StatusLayout
import com.kyle.colman.view.recyclerview.adapter.CommonAdapter
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import com.kyle.colman.view.recyclerview.adapter.PageAdapter
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
class RecyclerViewFragment<T : Any> :
    KFragment<FragmentRecyclerviewBinding>(R.layout.fragment_recyclerview) {
    val adapter by lazy {
        PageAdapter<T>(context!!)
    }
    val viewmodel by viewModels<RecyclerViewFragmentVModel>()

    override fun initView() {
        initRecyclerView()
        initStatusLayout()
        initRefreshLayout()
        adapter.addLoadResultCallback {
            swipe_refreshlayout.isRefreshing = false
            when (it) {
                is RESULT.COMPLETED -> {
                    statuslayout.showDefaultContent()
                }
                is RESULT.FAILED -> statuslayout.switchLayout(StatusLayout.STATUS_ERROR)
            }
        }
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.bindRecyclerView(recyclerview)
    }

    fun initPageSource(source: IPageSource<T>) {
        adapter.bindPageSource(source)
        adapter.refresh()
        statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
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
    }

    fun initRefreshLayout() {
        swipe_refreshlayout.setOnRefreshListener {
            adapter.refresh()
        }
    }
}