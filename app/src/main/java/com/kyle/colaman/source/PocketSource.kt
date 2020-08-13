package com.kyle.colaman.source

import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.kyle.colaman.entity.ArticleRoomEntity
import com.kyle.colaman.getPocketRoom
import com.kyle.colaman.helper.PocketRoom
import com.kyle.colman.view.KApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author   : kyle
 * Date     : 2020/6/23
 * Function : pocket数据提供源
 */

class PocketSource : PagingSource<Int, ArticleRoomEntity>() {
    val dao by lazy {
        getPocketRoom()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleRoomEntity> {
        val page = params.key ?: 0
        var pageSize = params.loadSize
        return try {
            withContext(Dispatchers.IO) {
                val datas = dao.getPocketArticles()
                LoadResult.Page(
                    datas, prevKey = if (params.key ?: 0 == 0) null else page - 1,
                    nextKey = if (datas.size < pageSize) null else page + 1
                )
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}