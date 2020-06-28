package com.kyle.colman.helper

import com.kyle.colman.network.KError
import kotlinx.coroutines.*

/**
 * Author   : kyle
 * Date     : 2020/6/28
 * Function : 協程封裝
 */
class KLaunch private constructor(val scope: CoroutineScope) {
    private lateinit var _block: (suspend CoroutineScope.() -> Unit)
    private var _doneBlock: (suspend CoroutineScope.() -> Unit)? = null
    private var _errorBlock: ((KError) -> Unit)? = null
    private var _startBlock: (suspend CoroutineScope.() -> Unit)? = null
    private var _dispatcher: CoroutineDispatcher = Dispatchers.Default

    companion object {
        fun get(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)): KLaunch =
            KLaunch(scope)
    }

    fun launch(block: (suspend CoroutineScope.() -> Unit)): KLaunch {
        _block = block
        return this
    }

    fun onDone(block: (suspend CoroutineScope.() -> Unit)): KLaunch {
        _doneBlock = block
        return this
    }

    fun onError(block: ((KError) -> Unit)): KLaunch {
        _errorBlock = block
        return this
    }

    fun onStart(block: (suspend CoroutineScope.() -> Unit)): KLaunch {
        _startBlock = block
        return this
    }

    fun dispatcher(dispatcher: CoroutineDispatcher) {
        _dispatcher = dispatcher
    }

    fun run() {
        scope.launch(Dispatchers.Main + kHandler {
            _errorBlock?.invoke(it)
        }) {
            _startBlock?.invoke(this)
            try {
                withContext(_dispatcher) {
                    _block.invoke(this)
                }
            } finally {
                _doneBlock?.invoke(this)
            }
        }
    }
}

