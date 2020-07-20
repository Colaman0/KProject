package com.kyle.colman.helper

import android.content.Context
import android.view.View
import androidx.paging.PagingSource
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import me.jessyan.autosize.utils.AutoSizeUtils
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function : 拓展
 */

fun CoroutineScope.catchLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    errorBlock: (suspend CoroutineScope.() -> Unit)? = null,
    finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
    runBlock: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    try {
        runBlock()
    } catch (e: Exception) {
        errorBlock?.let { it() }
    } finally {
        finallyBlock?.let { it() }
    }
}

/**
 * 处理api请求的基类
 *
 * @param T
 * @param dataNullable
 * @return
 */
fun <T> KResponse<T>.toData(dataNullable: Boolean = true): T? {
    if (success()) {
        responseData()?.let {
            return it
        }
    } else {
        throw ApiException(code = responseCode(), message = message())
    }
    if (dataNullable) {
        return null
    }
    throw DataNullException()
}


/**
 * 把throwable转换成[KError]，封装了错误类型和一些错误的固定提示语
 * 可以通过[KResponse.exceptionFilters]去添加一些过滤器
 * @return
 */
fun Throwable.toKError(): KError {
    var error: KError? = null
    if (this is ApiException) {
        return KError(kThrowable = this, KErrorType = KErrorType.Api)
    }
    KResponse.exceptionFilters.forEach { filter ->
        if (filter.isCreate(this)) {
            error = filter.createKError(this)
            filter.onCatch()
            return@forEach
        }
    }
    return error ?: KError(kThrowable = this, KErrorType = KErrorType.Unknown)
}

/**
 * 协程的通用异常捕获handler
 *
 * @param block
 */
fun kHandler(block: (KError) -> Unit) = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
    val error = exception.toKError()
    block(error)
}

fun catchError() {

}

fun <T> List<T>.isNotNullOrEmpty() = !isNullOrEmpty()

fun <T> List<T>.copy() = toCollection(mutableListOf())

/**
 * 把view的点击事件转换为flow，可以接着做debounce处理
 *
 * @return
 */
@ExperimentalCoroutinesApi
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        offer(Unit)
    }
    awaitClose { setOnClickListener(null) }
}


fun logd(text: String) {
    LogUtils.d(text)
}

fun loge(text: String) {
    LogUtils.e(text)
}

/**
 * 把flow流订阅在IO线程
 *
 * @param T
 */
fun <T> Flow<T>.io() = flowOn(Dispatchers.IO)

fun <T : Any> IPageDTO<T>.toPageResult(param: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, T> {
    // 如果key是null，那就加载第0页的数据
    val page = param.key ?: 0
    return PagingSource.LoadResult.Page(
        data = pageData(),
        prevKey = if (param.key ?: 0 == 0) null else page - 1,
        nextKey = if (isLastPage()) null else page + 1
    )
}


fun dp2px(context: Context, value: Int) = if (value > 0) {
    AutoSizeUtils.dp2px(context, value.toFloat())
} else {
    value
}

val Any.tags by lazy {
    return@lazy HashMap<String, Any>()
}

inline fun <reified T> Any.getTag(key: String): T? {
    val data = tags[key]
    if (data is T) {
        return data
    }
    return null
}

fun Any.putTag(key: String, value: Any) {
    tags[key] = value
}

/**
 * 作为布尔值判断的lamda简写
 */
fun Boolean?.yes(callback: () -> Unit) {
    if (this == true) {
        callback()
    }
}


fun Boolean?.no(callback: () -> Unit) {
    if (this == false) {
        callback()
    }
}



