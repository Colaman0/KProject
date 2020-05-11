package com.colaman.wanandroid.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.colaman.kyle.BR
import com.colaman.kyle.common.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.common.recyclerview.adapter.BaseViewHolder
import com.colaman.wanandroid.R
import com.colaman.wanandroid.databinding.ItemNaviActionBinding
import com.colaman.wanandroid.entity.NaviAction

/**
 * Author   : kyle
 * Date     : 2020/5/4
 * Function : 首页左边侧栏action  itemviewmodel
 */
class ItemNaviActionViewModel(val action: NaviAction) :
    RecyclerItemViewModel<ItemNaviActionBinding, ItemNaviActionViewModel>() {


    override fun initLayoutRes() = R.layout.item_navi_action

    override fun onBindView(holder: BaseViewHolder<ItemNaviActionBinding>) {
        binding?.setVariable(BR.action, action)
    }

    fun update(select: Boolean) {
        binding?.text?.setBackgroundColor(context!!.resources.getColor(if (select) R.color.color_4d4d4d else R.color.white))
        binding?.text?.setTextColor(context!!.resources.getColor(if (select) R.color.white else R.color.color_4d4d4d))
    }
}