package com.kyle.colaman.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.leanback.app.BaseFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.colaman.statuslayout.StatusLayout
import com.kyle.colaman.ActionFragment
import com.kyle.colaman.IActionFragment
import com.kyle.colaman.R
import com.kyle.colaman.databinding.FragmentTixiBinding
import com.kyle.colaman.entity.ActionTixi
import com.kyle.colaman.entity.NaviAction
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.viewmodel.TixiViewModel
import com.kyle.colman.view.KFragment
import kotlinx.android.synthetic.main.fragment_tixi.*

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系
 */
class TixiFragment : KFragment<FragmentTixiBinding>(R.layout.fragment_tixi), IActionFragment {
    //    val dataCreator = ArticleDataCreator(0)
    val viewModel: TixiViewModel by viewModels()

    override fun initView() {

//
//        viewModel.getTixi()
//        binding.fragment = this
//        binding.statusLayout.switchLayout(StatusLayout.STATUS_LOADING)
//        viewModel.lastId.observe(this, Observer {
//            dataCreator.id = it
//            fragment.refresh()
//        })
//        viewModel.tixiItems.observe(this, object : RxDataObserver<List<TixiEntity>> {
//            override fun onSuccess(data: List<TixiEntity>) {
//                viewModel.firstItem.set(data[0].name)
//                viewModel.secondItem.set(data[0].children!![0].name)
//                viewModel.getArticle(data[0].children!![0].id!!)
//                dataCreator.id = data[0].children!![0].id!!
//                fragment.refresh()
//                binding.statusLayout.showDefaultContent()
//            }
//
//            override fun onError(throwable: Throwable) {
//                super.onError(throwable)
//                statusLayout?.switchLayout(StatusLayout.STATUS_ERROR)
//            }
//        })
    }


    fun showBottomSelector() {
        val bottomFragment = TixiSelectorFragment(viewModel)
        bottomFragment.show(parentFragmentManager, "tixi")
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
}