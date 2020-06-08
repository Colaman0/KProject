package com.kyle.colaman.viewmodel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.SizeUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ItemArticleBinding
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.helper.CollectManager
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder
import com.like.LikeButton
import com.like.OnLikeListener
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

/**
 * Author   : kyle
 * Date     : 2020/5/7
 * Function : 文章item
 */
class ItemArticleViewModel(val entity: ArticleEntity?) :
    RecyclerItemView<ItemArticleBinding, ItemArticleViewModel>(R.layout.item_article) {


    var showItemTag = true
    val title = ObservableField<String>(entity?.title)
    val authorText by lazy {
        var text = ""
        if (entity?.author?.isNotEmpty() == true) {
            text = "作者：${entity.author}"
        } else if (entity?.shareUser?.isNotEmpty() == true) {
            text = "分享人：${entity.shareUser}"
        }
        text
    }

    val collectActionStream = PublishSubject.create<Boolean>()


    init {
        collectActionStream
            .debounce(500, TimeUnit.MILLISECONDS)
            .doOnNext {
                if (it) {
                    entity?.id?.let { it1 ->
                    }
                } else {
                    entity?.id?.let { it1 ->

                    }
                }
            }
            .subscribe()!!

    }

    override fun onBindView(holder: BaseViewHolder<ItemArticleBinding>) {
        binding?.tabLayout?.removeAllViews()
        entity?.title?.apply {
            binding?.tvTitle?.text = Html.fromHtml(this, Html.ImageGetter {
                ColorDrawable(Color.TRANSPARENT)
            }, null)
        }
        if (entity?.fresh == true) {
            binding?.tabLayout?.addView(createTagView("新"))
        }
        entity?.tags?.forEach {
            binding?.tabLayout?.addView(createTagView(it.name))
        }
        entity?.desc.apply {
            binding?.tvDesc?.text = Html.fromHtml(this, Html.ImageGetter {
                ColorDrawable(Color.TRANSPARENT)
            }, null)
        }
        binding?.starButton?.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                collectActionStream.onNext(true)
            }

            override fun unLiked(likeButton: LikeButton?) {
                collectActionStream.onNext(false)
            }

        })
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

    fun actionCheck() {

    }

    override fun isUISame(data: ItemArticleViewModel): Boolean {
        return entity?.id == data.entity?.id
    }

    override fun isItemSame(data: ItemArticleViewModel): Boolean {
        return entity?.id == data.entity?.id
    }

}
