package com.kyle.colaman.viewmodel

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemPocketBinding
import com.kyle.colaman.entity.ArticleRoomEntity
import com.kyle.colaman.gotoWeb
import com.kyle.colman.helper.FilterTime
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder
import com.tencent.smtt.utils.p

/**
 * Author   : kyle
 * Date     : 2020/6/24
 * Function : pocket item
 */
class ItemPocketViewmodel(
    val entity: ArticleRoomEntity,
    val removeCallback: (ArticleRoomEntity, Int) -> Unit
) :
    PagingItemView<ItemPocketViewmodel, ItemPocketBinding>(R.layout.item_pocket) {
    var position = -1
    override fun onBindView(holder: PagingVHolder, position: Int) {
        this.position = position
        binding?.apply {
            viewmodel = this@ItemPocketViewmodel
            tvDesc.text = Html.fromHtml(entity.desc, Html.ImageGetter {
                ColorDrawable(Color.TRANSPARENT)
            }, null)

        }
    }

    override fun onItemClick(position: Int) {
        super.onItemClick(position)
        gotoWeb(context as Activity, entity.link, entity.title, entity.articleId, entity.desc)
    }

    override fun areItemsTheSame(data: ItemPocketViewmodel): Boolean {
        return entity.articleId == data.entity.articleId
    }

    override fun areContentsTheSame(data: ItemPocketViewmodel): Boolean {
        return entity.articleId == data.entity.articleId
    }
}