package com.kyle.colaman.source

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingSource
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.CollectEntity
import com.kyle.colaman.viewmodel.ItemCollectViewmodel
import com.kyle.colman.helper.toData
import com.kyle.colman.helper.toKError
import com.kyle.colman.helper.toPageResult
import com.kyle.colman.impl.IPageSource
import com.kyle.colman.impl.PageResult
import com.kyle.colman.recyclerview.PagingItemView
import kotlinx.coroutines.CoroutineScope
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/19
 * Function : 收藏
 */
class CollectSource() : PagingSource<Int, CollectEntity>(), Serializable {
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

class CollectPageSource(coroutineScope: CoroutineScope,val lifecycleOwner: LifecycleOwner) :
    IPageSource<CollectEntity>(coroutineScope = coroutineScope) {
    override suspend fun onResult(pageSize: Int, page: Int): PageResult.SUCCESS<CollectEntity> {
        val response = Api.getCollectArticle(page).toData()
        return PageResult.SUCCESS(data = response!!.datas, nextPage = response.nextApiPage())
    }

    override fun resultToPageItem(data: CollectEntity): PagingItemView<*,*> {
        return ItemCollectViewmodel(data,lifecycleOwner = lifecycleOwner)
    }

}