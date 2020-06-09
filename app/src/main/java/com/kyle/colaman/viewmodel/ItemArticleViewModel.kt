package com.kyle.colaman.viewmodel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemArticleBinding
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.helper.CollectManager
import com.kyle.colman.helper.kHandler
import com.kyle.colman.view.CommonDialog
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/5/7
 * Function : 文章item
 */
class ItemArticleViewModel(val entity: ArticleEntity) :
    RecyclerItemView<ItemArticleBinding, ItemArticleViewModel>(R.layout.item_article) {
    val loadingDialog by lazy {
        CommonDialog(context!!)
    }
    val authorText by lazy {
        var text = ""
        if (entity.author.isNotEmpty()) {
            text = "作者：${entity.author}"
        } else if (entity.shareUser.isNotEmpty()) {
            text = "分享人：${entity.shareUser}"
        }
        text
    }
    val collectStatusObserver = Observer<Boolean> {
        entity.collect = it
        binding.starButton.isLiked = it
    }

    init {
        entity.let { CollectManager.putNewArticle(it) }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onBindView(holder: BaseViewHolder<ItemArticleBinding>) {
        lifecycleOwner?.let {
            CollectManager.getCollectLiveDataById(entity.id)
                .observe(it, collectStatusObserver)
        }
        binding.tabLayout.removeAllViews()
        entity.title.apply {
            binding.tvTitle.text = Html.fromHtml(this, Html.ImageGetter {
                ColorDrawable(Color.TRANSPARENT)
            }, null)
        }
        if (entity.fresh) {
            binding.tabLayout.addView(createTagView("新"))
        }
        entity.tags.forEach {
            binding.tabLayout.addView(createTagView(it.name))
        }
        entity.desc.apply {
            binding.tvDesc.text = Html.fromHtml(this, Html.ImageGetter {
                ColorDrawable(Color.TRANSPARENT)
            }, null)
        }
        // 过滤多余的点赞按钮事件
        callbackFlow<Boolean> {
            binding.starButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    offer(true)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    offer(false)
                }
            })
            awaitClose()
        }.debounce(1000).onEach {
            actionLike(it)
        }.launchIn(lifecycleOwner!!.lifecycleScope)
    }

    fun createTagView(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            textSize = 12f
            setTextColor(context.resources.getColor(R.color.colorPrimary))
            setPadding(
                SizeUtils.dp2px(8f),
                SizeUtils.dp2px(4f),
                SizeUtils.dp2px(8f),
                SizeUtils.dp2px(4f)
            )
            setBackgroundResource(R.drawable.shape_chip)
        }
    }

    /**
     * 网络请求前后把btn设成不可点击，避免ui跳动
     *
     * @param like
     */
    fun actionLike(like: Boolean) {
        if (like == entity.collect) {
            return
        }
        binding.starButton.isEnabled = false
        lifecycleOwner!!.lifecycleScope.launch(Dispatchers.IO + kHandler {
            // 请求失败之后重置回原本的收藏状态
            binding.starButton.isLiked = entity.collect
            binding.starButton.isEnabled = true
        }) {
            if (like) {
                entity.id.let { CollectManager.collect(it) }
            } else {
                entity.id.let { CollectManager.unCollect(it) }
            }
            // 重新设置entity里的收藏状态
            entity.collect = like
            binding.starButton.isLiked = like
            binding.starButton.isEnabled = true
        }
    }


    fun actionCheck() {

    }

    override fun isUISame(data: ItemArticleViewModel): Boolean {
        return entity.id == data.entity.id
    }

    override fun isItemSame(data: ItemArticleViewModel): Boolean {
        return entity.id == data.entity.id
    }

}
