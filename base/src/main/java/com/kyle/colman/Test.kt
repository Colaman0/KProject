package com.kyle.colman

import com.kyle.colman.coroutine.KLaunch
import kotlinx.coroutines.*
import java.lang.NullPointerException

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function : 测试
 */
fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default)
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    launch(handler) {
        println("chong")
        throw  NullPointerException()
    }
    KLaunch.get(scope)
        .launch {
            println("chong")
            throw NullPointerException("----")
        }
        .onStart { println("start") }
        .onDone { println("done") }
        .onError { println("error") }
        .run()
}