package com.kyle.colaman.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.blankj.utilcode.util.LogUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.base.recyclerview.PageConfig
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colman.helper.toKError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import okhttp3.internal.threadFactory

/**
 * Author   : kyle
 * Date     : 2020/6/15
 * Function : 分页
 */

class MainSource : PagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 如果key是null，那就加载第0页的数据
        val page = params.key ?: 0
        return try {
            val response = Api.getHomeArticles(page)
            LogUtils.d("加载第$page 页")
            LoadResult.Page(
                data = response.pageData(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.isLastPage()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e.toKError())
        }
    }

    val pager by lazy {
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 1),
            pagingSourceFactory = { this }).flow
    }
}

