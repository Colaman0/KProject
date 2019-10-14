package com.colaman.common.base.viewmodel

import com.colaman.common.base.recyclerview.adapter.BaseViewHolder
import com.colaman.common.base.recyclerview.adapter.LOADMORE_STATE
import com.colaman.common.R
import com.colaman.common.databinding.ItemLoadmoreBinding

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/1
 *     desc   : 默认的loadmoreitem
 * </pre>
 */
open class CommonLoadMoreVModel(var text: String = "") :
    BaseLoadmoreViewModel<ItemLoadmoreBinding, Any>() {
    override fun initLayouRes() = R.layout.item_loadmore


    override fun onStateChange(state: LOADMORE_STATE) {
        when (state) {
            LOADMORE_STATE.SUCCESS -> binding?.tvStatus?.text = "成功"
            LOADMORE_STATE.FAILED -> binding?.tvStatus?.text = "失败"
            LOADMORE_STATE.LOADING -> binding?.tvStatus?.text = "加载"
        }
    }

    override fun isSame(data: Any) = false

    override
    fun onBindView(holder: BaseViewHolder?) {

    }


}