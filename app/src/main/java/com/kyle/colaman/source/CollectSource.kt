package com.kyle.colaman.source

import androidx.paging.PagingSource
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.CollectEntity
import com.kyle.colman.helper.toData
import com.kyle.colman.helper.toKError
import com.kyle.colman.helper.toPageResult

/**
 * Author   : kyle
 * Date     : 2020/6/19
 * Function : 收藏
 */
class CollectSource() : PagingSource<Int, CollectEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectEntity> {
        val page = params.key ?: 0
        val size = params.loadSize
        return try {
            Api.getCollectArticle(page).toData()!!.toPageResult(params)
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable.toKError())
        }
    }
}