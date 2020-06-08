package com.colaman.wanandroid.viewmodel

import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemTextBinding
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : text
 *
 */
class ItemTextViewmodel(val text: String) :
    RecyclerItemView<ItemTextBinding, ItemTextViewmodel>(R.layout.item_text) {
    override fun onBindView(holder: BaseViewHolder<ItemTextBinding>) {
    }

    override fun isUISame(data: ItemTextViewmodel): Boolean {
        return false
    }

    override fun isItemSame(data: ItemTextViewmodel): Boolean {
        return false
    }

}