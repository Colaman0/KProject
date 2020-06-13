package com.kyle.colman.helper

import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
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
    KResponse.exceptionFilters.forEach { filter ->
        if (filter.isCreate(this)) {
            error = filter.createKError(this)
            filter.onCatch()
            return@forEach
        }
    }
    return error ?: KError(
        kThrowable = this,
        errorType = if (this is ApiException) ApiError else UnknownError
    )
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
    LogUtils.e(error)
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