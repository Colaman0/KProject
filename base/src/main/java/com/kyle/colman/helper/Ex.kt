package com.kyle.colman.helper

import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.kyle.colman.R
import com.kyle.colman.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

fun <T> KReponse<T>.toData(): T? {
    if (success()) {
        responseData()?.let {
            return it
        }
    } else {
        throw ApiException(code = responseCode(), message = message())
    }
    return null
}


fun Throwable.toKError(): KError {
    var error: KError? = null
    KReponse.exceptionFilters.forEach { filter ->
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

fun kHandler(block: (KError) -> Unit) = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
    val error = exception.toKError()
    block(error)
    LogUtils.e(error)
}

fun <T> List<T>.isNotNullOrEmpty() = !isNullOrEmpty()

fun <T> List<T>.copy() = toCollection(mutableListOf())

fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        offer(Unit)
    }
    awaitClose { setOnClickListener(null) }
}


