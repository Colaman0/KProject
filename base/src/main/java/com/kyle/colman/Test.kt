package com.kyle.colman

import com.kyle.colman.helper.cacheHistory
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.runBlocking

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function : 测试
 */
fun main() = runBlocking {
    val subject = PublishSubject.create<Int>()
    val observable = subject.cacheHistory()

    observable.subscribe {
        println("1 =  $it")
    }

    subject.onNext(1)
    observable
        .subscribe {
            println("2 =  $it")
        }
    subject.onNext(2)

}