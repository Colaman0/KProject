package com.kyle.colaman.helper

import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.view.recyclerview.RecyclerItemView
import kotlinx.coroutines.*
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : datacreator
 */

class MainDataCreator() :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        return withContext(Dispatchers.IO) {
            var data: IPageDTO<ArticleEntity>? = null
            if (page == 0) {
                val topArticles = async {
                    Api.getHomeTopArticles().apply {
                        forEach { it.topArticle = true }
                    }
                }
                val articles = async { Api.getHomeArticles(page) }
                val totals = articles.await()
                totals?.datas?.addAll(0, topArticles.await())
                data = totals
            } else {
                data = Api.getHomeArticles(page)!!
            }
            data!!
        }
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}


class GuangchangCreator() :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        return Api.getGuangchangArticles(page)!!
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}

class XiangmuCreator() :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        return Api.getProjects(page)!!
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}

class WendaCreator() :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        return Api.getWenda(page)!!
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}


class TixiCreator(var id: Int) :
    IRVDataCreator<ArticleEntity>, Serializable {
    override suspend fun loadDataByPage(page: Int): IPageDTO<ArticleEntity> {
        return Api.getTixiArticle(page, id.toString())!!
    }

    override suspend fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
        return ItemArticleViewModel(data)
    }
}



