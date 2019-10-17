package com.colaman.common.common.helper

import androidx.lifecycle.MutableLiveData
import com.colaman.common.common.NotStickyLiveData


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
            eventMap.put(target, NotStickyLiveData<T>())
        }
        return eventMap.get(target) as NotStickyLiveData<T>
    }

    fun getChannel(target: String): MutableLiveData<Any> {
        return getChannel(target, Any::class.java)
    }
}