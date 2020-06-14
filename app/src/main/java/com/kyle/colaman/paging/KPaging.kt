package com.kyle.colaman.paging

import androidx.paging.Pager
import androidx.paging.PagingSource
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colman.helper.toKError
import kotlinx.coroutines.flow.flow

/**
 * Author   : kyle
 * Date     : 2020/6/15
 * Function : 分页
 */

class MainSource : PagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        // 页码
        var page = params.key ?: 0

        val response = Api.getHomeArticles(page)
       Pager
        return try {
            LoadResult.Page(
                data = response.pageData(), prevKey = if (page == 0) null else --page,
                nextKey = if (response.isLastPage()) null else ++page
            )
        } catch (e: Exception) {
            LoadResult.Error(e.toKError())
        }
    }
}

