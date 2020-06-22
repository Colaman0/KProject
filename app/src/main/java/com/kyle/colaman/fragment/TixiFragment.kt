package com.kyle.colaman.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyle.colman.view.StatusLayout
import com.kyle.colaman.R
import com.kyle.colaman.activity.MainActivity
import com.kyle.colaman.databinding.FragmentTixiBinding
import com.kyle.colaman.entity.ActionTixi
import com.kyle.colaman.entity.NaviAction
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.config.StatusConfig
import com.kyle.colman.databinding.LayoutPagingErrorBinding
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.helper.bindPaingState
import com.kyle.colman.helper.toKError
import com.kyle.colman.others.StateObserver
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.KFragment
import kotlinx.android.synthetic.main.fragment_tixi.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系
 */
class TixiFragment :
    KFragment<FragmentTixiBinding>(R.layout.fragment_tixi),
    IActionFragment {
    val viewModel: TixiViewModel by viewModels()
    val bottomFragment by lazy {
        TixiSelectorFragment()
    }

    val adapter by lazy {
        PagingAdapter(context!!)
    }

    val loadmoreAdapter = LoadMoreAdapter {
        this.adapter.retry()
    }


    companion object {
        fun newInstance(): TixiFragment {
            val args = Bundle()
            val fragment = TixiFragment()
            fragment.arguments = args
            return fragment
        }
    }

    init {
        lifecycleScope.launchWhenResumed {
            lazyLoadView()
            initRecyclerview()
            adapter.addLoadStateListener {
                if (it.refresh is LoadState.Error) {
                    loadmoreAdapter.loadState = LoadState.NotLoading(endOfPaginationReached = true)
                }
            }
            initStatusLayout()
            viewModel.initTixiHeader()
        }
    }

    var searchJob: Job? = null

    fun lazyLoadView() {
        binding.fragment = this
        binding.viewmodel = viewModel
        val errorBinding = LayoutPagingErrorBinding.inflate(LayoutInflater.from(context!!))

        top_status_layout.add(StatusConfig(StatusLayout.STATUS_LOADING, R.layout.layout_loading))
        top_status_layout.add(
            StatusConfig(
                status = StatusLayout.STATUS_ERROR,
                view = errorBinding.root,
                clickRes = R.id.btn_reload
            )
        )
        top_status_layout.setLayoutClickListener(object :
            StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status == StatusLayout.STATUS_ERROR) {
                    recyclerview_statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
                    viewModel.initTixiHeader()
                }
            }
        })
        viewModel.lastId.observe(this, Observer {
            searchJob?.cancel()
            searchJob = viewModel.viewModelScope.launch {
                viewModel.getArticlePager(it).collectLatest {
                    swipe_refreshlayout.isRefreshing = true
                    adapter.submitItem(it.map {
                        ItemArticleViewModel(it, this@TixiFragment)
                    })
                }
            }
        })
        viewModel.tixiItems.observe(this, StateObserver(
            loading = {
                top_status_layout.switchLayout(StatusLayout.STATUS_LOADING)
            }, fail = {
                top_status_layout.switchLayout(StatusLayout.STATUS_ERROR)
            }) { data ->
            viewModel.updateNewItemInfo(data[0].name ?: "", data[0].children!![0].name ?: "")
            viewModel.lastId.postValue(data[0].children!![0].id)
            top_status_layout.showDefaultContent()
        })
    }

    private fun initRecyclerview() {
        recyclerview.apply {
            adapter = this@TixiFragment.adapter.withLoadStateFooter(footer = loadmoreAdapter)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerview.setRecycledViewPool(MainActivity.pool)
        swipe_refreshlayout.bindPagingAdapter(adapter)
    }


    @OptIn(ExperimentalPagingApi::class)
    fun initStatusLayout() {
        recyclerview_statuslayout.add(
            StatusConfig(
                StatusLayout.STATUS_LOADING,
                R.layout.layout_loading
            )
        )
        val errorBinding = LayoutPagingErrorBinding.inflate(LayoutInflater.from(context!!))
        recyclerview_statuslayout.bindPaingState(adapter) {
            errorBinding.tvMessage.text = it.toKError().kTips
        }
        recyclerview_statuslayout.add(
            StatusConfig(
                status = StatusLayout.STATUS_ERROR,
                view = errorBinding.root,
                clickRes = R.id.btn_reload
            )
        )
        recyclerview_statuslayout.setLayoutClickListener(object :
            StatusLayout.OnLayoutClickListener {
            override fun OnLayoutClick(view: View, status: String?) {
                if (status == StatusLayout.STATUS_ERROR) {
                    adapter.retry()
                    recyclerview_statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
                }
            }
        })
        recyclerview_statuslayout.switchLayout(StatusLayout.STATUS_LOADING)
    }

    fun showBottomSelector() {
        bottomFragment.show(childFragmentManager, "tixi")
    }

    override fun findAction(): NaviAction {
        return ActionTixi
    }

    override fun findFragment(): Fragment {
        return this
    }

    override fun scrollTop() {
//        (recyclerview.layoutManager as LinearLayoutManager).scrollToPosition(
//            0
//        )
    }

    override fun initView() {
    }
}