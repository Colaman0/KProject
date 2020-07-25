package com.kyle.colaman.helper

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colaman.viewmodel.MainViewModel
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.recyclerview.KPagingSource
import com.kyle.colman.view.recyclerview.RecyclerItemView
import kotlinx.coroutines.*
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : datacreator
 */

class MainSource() : PagingSource<Int, ArticleEntity>(), Serializable {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        // 每一页的数据长度
        val pageSize = params.loadSize
        return try {
            var data: IPageDTO<ArticleEntity>?

            if (page == 0) {
                val topArticles =
                    Api.getHomeTopArticles().apply {
                        forEach { it.topArticle = true }
                    }
                val articles = Api.getHomeArticles(page)
                val totals = articles
                totals.datas.addAll(0, topArticles)
                data = totals
            } else {
                data = Api.getHomeArticles(page)
            }
            LoadResult.Page(
                data = data.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.over) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


class GuangchangCreator() : KPagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        // 每一页的数据长度
        val pageSize = params.loadSize
        return try {
            val data = Api.getGuangchangArticles(page)
            LoadResult.Page(
                data = data!!.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.over) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class XiangmuCreator() : KPagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        // 每一页的数据长度
        val pageSize = params.loadSize
        return try {
            val data = Api.getProjects(page)
            LoadResult.Page(
                data = data!!.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.over) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


class WendaCreator() : KPagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        // 每一页的数据长度
        val pageSize = params.loadSize
        return try {
            val data = Api.getWenda(page)
            LoadResult.Page(
                data = data!!.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.over) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


class TixiCreator(var id: Int) : KPagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: PagingSource.LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        // 每一页的数据长度
        val pageSize = params.loadSize
        return try {
            val data = Api.getTixiArticle(page, id)
            delay(2000)
            LoadResult.Page(
                data = data!!.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.over) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


