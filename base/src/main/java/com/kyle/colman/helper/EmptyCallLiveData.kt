package com.kyle.colman.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Author   : kyle
 * Date     : 2020/6/4
 * Function : 没有订阅者会回调通知的livedata
 */
class EmptyCallLiveData<T>(initValue: T? = null, val callback: ((EmptyCallLiveData<T>) -> Unit)?) : MutableLiveData<T>(initValue) {


    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        if (!hasObservers()) {
            callback?.invoke(this)
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        super.removeObservers(owner)
        if (!hasObservers()) {
            callback?.invoke(this)
        }
    }
}