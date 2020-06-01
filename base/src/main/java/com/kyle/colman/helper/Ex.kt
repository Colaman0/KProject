package com.kyle.colman.helper

import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.network.ApiException
import com.kyle.colman.network.KError
import com.kyle.colman.network.KReponse
import com.kyle.colman.network.UnkownError
import kotlinx.coroutines.*
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

fun <T> KReponse<T>.reponse(): T? {
    if (success()) {
        responseData()?.let {
            return it
        }
    } else {
        throw ApiException(code = responseCode(), message = message())
    }
    return null
}

suspend fun catchApiBlock(
    errorBlock: (suspend CoroutineScope.() -> Unit)? = null,
    finallyBlock: (suspend CoroutineScope.() -> Unit)? = null,
    scope: CoroutineScope,
    runBlock: suspend CoroutineScope.() -> Unit
) {
    try {
        runBlock(scope)
    } catch (e: Exception) {

    } finally {
        finallyBlock?.let { it(scope) }
    }
}

fun Throwable.toKError(): KError {
    KReponse.exceptionFilters.forEach { filter ->
        if (filter.isCreate(this)) {
            return filter.createKError(this)
        }
    }
    return KError(kThrowable = this, errorType = UnkownError())
}

fun kHandler(block: (KError) -> Unit) = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
    val error = exception.toKError()
    block(error)
    LogUtils.e(error)
}
