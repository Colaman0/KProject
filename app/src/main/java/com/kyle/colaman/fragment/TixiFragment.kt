package com.kyle.colaman.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.colaman.statuslayout.StatusLayout
import com.kyle.colaman.R
import com.kyle.colaman.databinding.FragmentTixiBinding
import com.kyle.colaman.entity.ActionTixi
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.NaviAction
import com.kyle.colaman.helper.TixiCreator
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.helper.bindPagingAdapter
import com.kyle.colman.others.StateObserver
import com.kyle.colman.recyclerview.LoadMoreAdapter
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.view.LazyFragment
import kotlinx.android.synthetic.main.fragment_tixi.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系
 */
class TixiFragment : LazyFragment<FragmentTixiBinding>(R.layout.fragment_tixi),
    IActionFragment {
    val dataCreator = TixiCreator(0)
    val viewModel: TixiViewModel by viewModels()
    val bottomFragment by lazy {
        TixiSelectorFragment()
    }
    val adapter by lazy {
        PagingAdapter(context!!).apply {
            withLoadStateFooter(LoadMoreAdapter {
                retry()
            })
        }
    }
    val pager by lazy {
        Pager<Int, ArticleEntity>(
            pagingSourceFactory = { TixiCreator(0) },
            config = PagingConfig(20, prefetchDistance = 1, initialLoadSize = 0)
        )
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
            viewModel.initTixiHeader()
        }
    }

    @ExperimentalCoroutinesApi
    override fun initView() {
        binding.fragment = this
        binding.viewmodel = viewModel
        binding.statusLayout.switchLayout(StatusLayout.STATUS_LOADING)
        binding.refreshRecyclerview.swipeRefreshlayout.bindPagingAdapter(adapter)
        binding.refreshRecyclerview.recyclerview.apply {
            adapter = this@TixiFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.lastId.observe(this, Observer {
            dataCreator.id = it
            adapter.refresh()
        })
        viewModel.tixiItems.observe(this, StateObserver(
            loading = {
                statusLayout?.switchLayout(StatusLayout.STATUS_LOADING)
            }, fail = {
                statusLayout?.switchLayout(StatusLayout.STATUS_ERROR)
            }) { data ->
            viewModel.updateNewItemInfo(data[0].name ?: "", data[0].children!![0].name ?: "")
            viewModel.lastId.postValue(data[0].children!![0].id)
            binding.statusLayout.showDefaultContent()
        })
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

    override fun lazyLoad() {

    }
}