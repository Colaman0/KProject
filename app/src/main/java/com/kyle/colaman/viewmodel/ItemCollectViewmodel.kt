package com.kyle.colaman.viewmodel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemCollectBinding
import com.kyle.colaman.entity.CollectEntity
import com.kyle.colaman.helper.CollectManager
import com.kyle.colman.helper.kHandler
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder
import com.kyle.colman.view.CommonDialog
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/6/22
 * Function : 收藏列表item
 */
class ItemCollectViewmodel(
    val entity: CollectEntity,
    val lifecycleOwner: LifecycleOwner
) :
    PagingItemView<ItemCollectViewmodel, ItemCollectBinding>(R.layout.item_collect) {
    var itemPosition = 0
    var unCollectCallback: ((Int) -> Unit)? = null
    val collectObserver = Observer<Boolean> {
        if (!it) {
            isRemoved = true
            unCollectCallback?.invoke(itemPosition)
        }
    }
    val loadingDialog by lazy {
        CommonDialog(context!!)
    }

    init {
        entity.let { CollectManager.putNewArticle(entity.originId!!, true) }
    }

    fun showConfirmDialog() {
        context?.let {
            MaterialDialog(it).show {
                title(text = "确认取消收藏该文章?")
                positiveButton(text = "确认", click = { unCollectArticle() })
                negativeButton(text = "取消", click = {
                    binding?.starButton?.isLiked = true
                    dismiss()
                })
            }
        }
    }


    fun unCollectArticle() {
        loadingDialog.show()
        lifecycleOwner.lifecycleScope.launch(kHandler {
            binding?.starButton?.isLiked = true
            loadingDialog.dismiss()
        } + Dispatchers.IO) {
            CollectManager.unCollect(entity.originId!!)
            binding?.starButton?.isLiked = false
            loadingDialog.dismiss()
        }
    }


    override fun onBindView(holder: PagingVHolder, position: Int) {
        itemPosition = position
        CollectManager.getCollectLiveDataById(entity.originId!!)
            ?.observe(lifecycleOwner, collectObserver)
        binding?.run {
            viewmodel = this@ItemCollectViewmodel
            // 作者文案
            if (entity.author!!.isNotEmpty()) {
                tvAuthor.text = "作者：${entity.author}"
            } else if (entity.shareUser!!.isNotEmpty()) {
                tvAuthor.text = "分享人：${entity.shareUser}"
            }

            entity.title.apply {
                tvTitle.text = Html.fromHtml(this, Html.ImageGetter {
                    ColorDrawable(Color.TRANSPARENT)
                }, null)
            }

            entity.desc.apply {
                tvDesc.text = Html.fromHtml(this, Html.ImageGetter {
                    ColorDrawable(Color.TRANSPARENT)
                }, null)
            }

            starButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {

                }

                override fun unLiked(likeButton: LikeButton?) {
                    showConfirmDialog()
                }
            })
        }
    }

    override fun areItemsTheSame(data: ItemCollectViewmodel): Boolean {
        return entity.id == data.entity.id
    }

    override fun areContentsTheSame(data: ItemCollectViewmodel): Boolean {
        return entity.id == data.entity.id
    }
}