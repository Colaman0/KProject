package com.kyle.colaman

import com.kyle.colman.helper.Catcher
import com.kyle.colman.helper.catchLaunch
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.system.measureTimeMillis

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function : test
 */

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    val deferred = GlobalScope.async(handler) { // also root, but async instead of launch
        throw ArithmeticException() // 没有打印任何东西，依赖用户去调用 deferred.await()
    }

    joinAll(deferred)
}