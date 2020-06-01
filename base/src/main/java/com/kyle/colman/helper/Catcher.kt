package com.kyle.colman.helper

import com.kyle.colman.network.*
import kotlinx.coroutines.CoroutineScope
import java.lang.Exception

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : 捕获异常类
 */
class Catcher(val scope: CoroutineScope) {
    var errorBlock: (suspend CoroutineScope.(KError) -> Unit)? = null
    var finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    var runBlock: (suspend CoroutineScope.() -> Unit)? = null

    val exceptionFilters = mutableListOf<IExceptionFilter>(JsonFilter(), NetworkFilter())

    fun error(block: suspend CoroutineScope.(KError) -> Unit): Catcher {
        errorBlock = block
        return this
    }

    fun run(block: suspend CoroutineScope.() -> Unit): Catcher {
        runBlock = block
        return this
    }

    fun finally(block: suspend CoroutineScope.() -> Unit): Catcher {
        finallyBlock = block
        return this
    }

    suspend fun start() {
        runBlock?.let { runBlock ->
            try {
                runBlock(scope)
            } catch (e: Exception) {
                errorBlock?.let { errorBlock -> errorBlock(scope, dealError(e)) }
            } finally {
                finallyBlock?.let { finallyBlock -> finallyBlock(scope) }
            }
        }
    }

    fun dealError(e: Exception): KError {
        exceptionFilters.forEach {
            if (it.isCreate(e)) {
                return it.createKError(e)
            }
        }
        return KError(kThrowable = e, errorType = UnkownError())
    }
}

