package com.kyle.colaman

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.activity.MainActivity
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colaman.entity.NaviAction
import com.kyle.colman.config.Constants
import com.kyle.colman.impl.IRVDataCreator
import kotlinx.android.synthetic.main.fragment_action.*

/**
 * Author   : kyle
 * Date     : 2020/5/13
 * Function : 首页不同Tab的fragment基类
 */
class ActionFragment<T>() :
    Fragment(R.layout.fragment_action), IActionFragment {
    val action by lazy {
        arguments?.getSerializable("action") as NaviAction
    }
    val creator by lazy {
        arguments?.getSerializable("creator") as IRVDataCreator<T>
    }
    val viewmodel by viewModels<BaseViewModel>()


    init {
        lifecycleScope.launchWhenResumed {
            refresh_recyclerview.setDataCreator(creator)
            refresh_recyclerview.getRecyclerview().setRecycledViewPool(MainActivity.pool)
            refresh_recyclerview.getRefreshView().startRefresh()
        }
    }

    companion object {
        fun <T> newInstance(action: NaviAction, creator: IRVDataCreator<T>): ActionFragment<T> {
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
        refresh_recyclerview.getRecyclerview().smoothScrollToPosition(0)
    }
}


interface IActionFragment {
    fun findAction(): NaviAction

    fun findFragment(): Fragment

    fun scrollTop()
}