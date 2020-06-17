package com.kyle.colaman.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.activity.MainActivity
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.NaviAction
import com.kyle.colaman.viewmodel.ActionViewModel
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colman.databinding.LayoutPagingRecyclerviewBinding
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.recyclerview.KPagingSource
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/5/13
 * Function : 首页不同Tab的fragment基类
 */
class ActionFragment<T>() :
    Fragment(R.layout.fragment_action),
    IActionFragment {
    val action by lazy {
        arguments?.getSerializable("action") as NaviAction
    }
    val source by lazy {
        arguments?.getSerializable("creator") as PagingSource<Int, ArticleEntity>
    }
    val viewmodel by viewModels<ActionViewModel>()
    val recyclerviewBinding by lazy {
        LayoutPagingRecyclerviewBinding.bind(paging_recyclerview)
    }
    val adapter by lazy {
        PagingAdapter(context!!)
    }
    val pager by lazy {
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 3),
            pagingSourceFactory = { source }
        ).flow.cachedIn(viewmodel.viewModelScope)
    }
    val loadmoreAdapter = LoadMoreAdapter {
        this@ActionFragment.adapter.retry()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerviewBinding.recyclerview.apply {
            adapter = this@ActionFragment.adapter.withLoadStateFooter(footer = loadmoreAdapter)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerviewBinding.recyclerview.setRecycledViewPool(MainActivity.pool)

        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                loadmoreAdapter.loadState = LoadState.NotLoading(endOfPaginationReached = true)
            }
        }

        recyclerviewBinding.swipeRefreshlayout.bindPagingAdapter(adapter)
        lifecycleScope.launch {
            pager.collect {
                adapter.submitItem(it.map {
                    ItemArticleViewModel(it, this@ActionFragment)
                })
            }
        }
    }

    companion object {
        fun <T : Any> newInstance(
            action: NaviAction,
            creator: KPagingSource<Int, T>
        ): ActionFragment<T> {
            val args = Bundle()
            args.putSerializable("action", action)
            args.putSerializable("creator", creator)
            val fragment = ActionFragment<T>()
            fragment.arguments = args
            return fragment
        }
    }

    override fun findAction(): NaviAction {
        return action
    }

    override fun findFragment(): Fragment {
        return this
    }


    override fun scrollTop() {
        recyclerviewBinding.recyclerview.smoothScrollToPosition(0)
    }
}


interface IActionFragment {
    fun findAction(): NaviAction

    fun findFragment(): Fragment

    fun scrollTop()
}