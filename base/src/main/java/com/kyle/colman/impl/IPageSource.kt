package com.kyle.colman.impl

import androidx.databinding.ViewDataBinding
import com.kyle.colman.coroutine.KLaunch
import com.kyle.colman.recyclerview.PagingItemView
import kotlinx.coroutines.CoroutineScope

/**
 * Author   : kyle
 * Date     : 2020/7/16
 * Function : 分页数据源
 */
abstract class IPageSource<T>(
    val firstPage: Int = 0,
    val pageSize: Int = 20,
    val coroutineScope: CoroutineScope
) {
    var nextPage: Int? = firstPage
    lateinit var dataCallback: (PageResult) -> Unit

    abstract suspend fun onResult(pageSize: Int, page: Int): PageResult.SUCCESS<T>

    abstract fun resultToPageItem(data: T): PagingItemView<Any, ViewDataBinding>

    /**
     * 刷新数据
     *
     */
    fun refresh() {
        nextPage = firstPage
        loadSource(nextPage)
    }

    /**
     * 加载下一页
     * */
    fun loadNextPage() {
        loadSource(nextPage)
    }

    private fun loadSource(page: Int?) {
        if (page != null) {
            KLaunch.get(coroutineScope)
                .launch {
                    val result = onResult(pageSize, page)
                    nextPage = result.nextPage
                    dataCallback.invoke(result)
                }
                .onError {
                    dataCallback.invoke(PageResult.ERROR(it))
                }
                .run()
        }
    }

    /**
     * 监听数据变化
     *
     * @param callback
     */
    fun listen(callback: (PageResult) -> Unit) {
        dataCallback = callback
    }
}

sealed class PageResult {
    data class SUCCESS<T>(val data: List<T>, val nextPage: Int?) : PageResult()
    data class ERROR(val throwable: Throwable) : PageResult()
}
