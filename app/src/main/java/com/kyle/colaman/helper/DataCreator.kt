package com.kyle.colaman.helper

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.PageDTO
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.view.recyclerview.RecyclerItemView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : datacreator
 */

class MainDataCreator(val viewmodelScope: CoroutineScope) :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        delay(3000)
        if (page == 0) {
            val topArticles = viewmodelScope.async {
                Api.getHomeTopArticles().apply {
                    forEach { it.topArticle = true }
                }
            }
            val articles = viewmodelScope.async { Api.getHomeArticles(page) }
            val totals = articles.await()
            totals?.datas?.addAll(0, topArticles.await())
            return totals!!
        } else {
            return Api.getHomeArticles(page)!!
        }
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}