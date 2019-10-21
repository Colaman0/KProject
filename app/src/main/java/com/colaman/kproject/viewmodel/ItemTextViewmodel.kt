package com.colaman.kproject.viewmodel

import com.colaman.kyle.base.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.base.recyclerview.adapter.BaseViewHolder
import com.colaman.kproject.R
import com.colaman.kproject.databinding.ItemTextBinding

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : text
 *
 */
class ItemTextViewmodel(val text: String) :
    RecyclerItemViewModel<ItemTextBinding, ItemTextViewmodel>() {
    override fun initLayouRes() = R.layout.item_text

    override fun onBindView(holder: BaseViewHolder?) {

    }

}