package com.kyle.colaman.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.colaman.statuslayout.StatusLayout
import com.kyle.colaman.IActionFragment
import com.kyle.colaman.R
import com.kyle.colaman.databinding.FragmentTixiBinding
import com.kyle.colaman.entity.ActionTixi
import com.kyle.colaman.entity.NaviAction
import com.kyle.colaman.helper.TixiCreator
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.others.StateObserver
import com.kyle.colman.view.LazyFragment
import kotlinx.android.synthetic.main.fragment_tixi.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系
 */
class TixiFragment : LazyFragment<FragmentTixiBinding>(R.layout.fragment_tixi), IActionFragment {
    val dataCreator = TixiCreator(0)
    val viewModel: TixiViewModel by viewModels()
    val bottomFragment by lazy {
        val fragment = TixiSelectorFragment()

        fragment
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
        binding.refreshRecyclerview.setDataCreator(datacreator = dataCreator)
        viewModel.lastId.observe(this, Observer {
            dataCreator.id = it
            binding.refreshRecyclerview.getRefreshView().startRefresh()
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
        (refresh_recyclerview.getRecyclerview().layoutManager as LinearLayoutManager).scrollToPosition(
            0
        )
    }

    override fun lazyLoad() {
        LogUtils.d("lazy load")
    }
}