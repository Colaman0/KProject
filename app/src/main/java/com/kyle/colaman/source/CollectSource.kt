package com.kyle.colaman.source

import androidx.paging.PagingSource
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colman.helper.toData
import com.kyle.colman.helper.toKError
import com.kyle.colman.helper.toPageResult
import com.tencent.smtt.utils.j
import java.lang.Exception

/**
 * Author   : kyle
 * Date     : 2020/6/19
 * Function : 收藏
 */
class CollectSource() : PagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        val page = params.key ?: 0
        val size = params.loadSize
        return try {
            Api.getCollectArticle(page).toData()!!.toPageResult(params)
        } catch (e: Exception) {
            LoadResult.Error(e.toKError())
        }
    }
}