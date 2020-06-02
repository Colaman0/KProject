package com.kyle.colman.helper

import androidx.lifecycle.MutableLiveData


/**
 *
 *     author : kyle
 *     time   : 2019/10/17
 *     desc   : livedatabus分发事件
 *
 */
object LivedataBus {
    private val eventMap by lazy {
        HashMap<String, NotStickyLiveData<*>>()
    }

    fun <T> getChannel(target: String, type: Class<T>): NotStickyLiveData<T> {
        if (!eventMap.containsKey(target)) {
            eventMap[target] = NotStickyLiveData<T>()
        }
        return eventMap[target] as NotStickyLiveData<T>
    }

    fun getChannel(target: String): MutableLiveData<Any> {
        return getChannel(target, Any::class.java)
    }
}