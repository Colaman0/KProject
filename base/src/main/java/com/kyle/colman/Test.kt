package com.kyle.colman

import com.kyle.colman.helper.cachedIt
import com.tencent.smtt.utils.v
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Funon : 测试
 */
fun main() = runBlocking {
    val flow = MutableStateFlow(0)

    launch {
        flow.collect { data -> println("data = $data") }
    }
    delay(1000)
    flow.value = 1
    delay(1000)
    flow.value = 2
    delay(1000)

    delay(1000)

    flow.value = 3
    delay(1000)

}