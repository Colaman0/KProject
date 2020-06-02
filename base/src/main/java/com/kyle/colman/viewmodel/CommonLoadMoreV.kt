package com.kyle.colman.viewmodel

import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.base.viewmodel.BaseLoadmoreView
import com.kyle.colman.R
import com.kyle.colman.databinding.ItemLoadmoreBindingImpl
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/1
 *     desc   : 默认的loadmoreitem
 * </pre>
 */
open class CommonLoadMoreV(var text: String = "") :
        BaseLoadmoreView<ItemLoadmoreBindingImpl, Any>( R.layout.item_loadmore) {

    override fun isSame(data: Any) = false


    override fun onBindView(holder: BaseViewHolder<ItemLoadmoreBindingImpl>) {

    }

    override fun startLoadMore() {
    }

    override fun endLoadMore() {
    }


    override fun onViewAttached() {
        super.onViewAttached()
        LogUtils.d("加载更多item显示")
    }

}