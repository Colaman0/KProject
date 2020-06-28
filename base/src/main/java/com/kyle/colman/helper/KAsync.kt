package com.kyle.colman.helper

import com.kyle.colman.network.KError
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * Author   : kyle
 * Date     : 2020/6/28
 * Function : 協程封裝
 */
class KAsync<T> private constructor(val scope: CoroutineScope) {
    private lateinit var _block: (suspend CoroutineScope.() -> T)
    private var _doneBlock: (suspend CoroutineScope.() -> Unit)? = null
    private var _errorBlock: ((KError) -> Unit)? = null
    private var _startBlock: (suspend CoroutineScope.() -> Unit)? = null
    private var _dispatcher: CoroutineDispatcher = Dispatchers.Default
    private lateinit var _deferred: Deferred<T>

    companion object {
        fun <T> get(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) =
            KAsync<T>(scope)
    }

    fun async(block: (suspend CoroutineScope.() -> T)): KAsync<T> {
        _block = block
        return this
    }

    fun onDone(block: (suspend CoroutineScope.() -> Unit)): KAsync<T> {
        _doneBlock = block
        return this
    }

    fun onError(block: ((KError) -> Unit)): KAsync<T> {
        _errorBlock = block
        return this
    }

    fun onStart(block: (suspend CoroutineScope.() -> Unit)): KAsync<T> {
        _startBlock = block
        return this
    }

    fun dispatcher(dispatcher: CoroutineDispatcher) {
        _dispatcher = dispatcher
    }

    suspend fun run(): KAsync<T> {
        _deferred = scope.async(Dispatchers.Main) {
            _startBlock?.invoke(this)
            withContext(_dispatcher) {
                _block.invoke(this)
            }
        }
        return this
    }


    suspend fun await(): T? {
        try {
            return _deferred.await()
        } catch (e: Throwable) {
            _errorBlock?.invoke(e.toKError())
        } finally {
            _doneBlock?.invoke(scope)
        }
        return null
    }
}

