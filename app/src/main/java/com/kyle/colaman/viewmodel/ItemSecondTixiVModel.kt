package com.colaman.wanandroid.viewmodel

import com.google.android.material.chip.Chip
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemSecondTixiBinding
import com.kyle.colaman.entity.SecondTixi
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 二级体系item viewmodel
 */
class ItemSecondTixiVModel(val datas: List<SecondTixi>) : RecyclerItemView<ItemSecondTixiBinding, ItemSecondTixiVModel>(
    R.layout.item_second_tixi) {

    override fun onBindView(holder: BaseViewHolder<ItemSecondTixiBinding>) {
        for (data in datas) {
            binding?.chipGroup?.addView(Chip(context).apply {
                text = data.name
            })
        }
    }
}