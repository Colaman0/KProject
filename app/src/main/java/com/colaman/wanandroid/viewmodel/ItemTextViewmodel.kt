package com.colaman.wanandroid.viewmodel

import com.colaman.kyle.common.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.common.recyclerview.adapter.BaseViewHolder
import com.colaman.wanandroid.R
import com.colaman.wanandroid.databinding.ItemTextBinding

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : text
 *
 */
class ItemTextViewmodel(val text: String) :
    RecyclerItemViewModel<ItemTextBinding, ItemTextViewmodel>() {
    override fun onBindView(holder: BaseViewHolder<ItemTextBinding>) {
    }

    override fun initLayoutRes() = R.layout.item_text


}