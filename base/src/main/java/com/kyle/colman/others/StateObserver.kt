package com.kyle.colman.others

import androidx.lifecycle.Observer
import com.kyle.colman.network.KError

/**
 * Author   : kyle
 * Date     : 2020/6/10
 * Function : 配合StateLiveData使用，封装订阅处理，只做了分发处理
 */

class StateObserver<T>(
    val fail: ((error: KError) -> Unit)? = null,
    val loading: (() -> Unit)? = null,
    val completed: (() -> Unit)? = null,
    val success: (data: T) -> Unit

) : Observer<STATE<T>> {
    override fun onChanged(state: STATE<T>?) {
        when (state) {
            is SUCCESS -> success.invoke(state.data!!)
            is FAIL -> fail?.invoke(state.throwable)
            is LOADING -> loading?.invoke()
            is COMPLETED -> completed?.invoke()
        }
    }
}