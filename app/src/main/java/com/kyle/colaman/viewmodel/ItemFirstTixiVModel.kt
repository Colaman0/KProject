package com.kyle.colaman.viewmodel

import android.view.View
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemFirstTixiBinding
import com.kyle.colaman.entity.SecondTixi
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colman.impl.ISelect
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 一级体系item
 */
class ItemFirstTixiVModel(
    var isCheck: Boolean = false,
    val entity: TixiEntity,
    val callBack: (List<SecondTixi>) -> Unit
) :
    RecyclerItemView<ItemFirstTixiBinding, ItemFirstTixiVModel>(R.layout.item_first_tixi),
    ISelect {


    override fun onBindView(holder: BaseViewHolder<ItemFirstTixiBinding>) {
        onChange(isCheck)
    }

    override fun onItemClick(position: Int, itemView: View?) {
        super.onItemClick(position, itemView)
        callBack.invoke(entity.children ?: listOf())
    }

    override fun onChange(select: Boolean) {
        isCheck = select
        binding?.textview?.isSelected = select
        binding?.textview?.setTextColor(context!!.resources.getColor(if (select) R.color.white else R.color.black))
    }

    override fun isUISame(data: ItemFirstTixiVModel): Boolean {
        return return entity?.id == data.entity?.id

    }

    override fun isItemSame(data: ItemFirstTixiVModel): Boolean {
        return return entity?.id == data.entity?.id

    }

}