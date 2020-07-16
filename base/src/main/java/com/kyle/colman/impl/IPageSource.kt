package com.kyle.colman.impl

import com.kyle.colman.coroutine.KLaunch
import kotlinx.coroutines.CoroutineScope

/**
 * Author   : kyle
 * Date     : 2020/7/16
 * Function : 分页数据源
 */
abstract class IPageSource(
    val firstPage: Int = 0,
    val pageSize: Int = 20,
    val coroutineScope: CoroutineScope
) {
    var nextPage = firstPage
    lateinit var dataCallback: (PageResult) -> Unit

    abstract suspend fun onResult(pageSize: Int, page: Int): PageResult.SUCCESS

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

    private fun loadSource(page: Int) {
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
    data class SUCCESS(val data: List<Any>, val nextPage: Int) : PageResult()
    data class ERROR(val throwable: Throwable) : PageResult()
}
