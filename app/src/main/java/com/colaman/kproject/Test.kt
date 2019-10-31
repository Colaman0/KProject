package com.colaman.kproject

import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.common.rx.httpRequest
import com.colaman.kyle.common.rx.transformItem
import io.reactivex.Flowable
import io.reactivex.Observable
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

/**
 *
 *     author : kyle
 *     time   : 2019/10/23
 *     desc   : d
 *
 */

fun main(args: Array<String>) {
    Observable.just(mutableListOf(1,2,3,4,5,6))
        .transformItem {
            "$it !!!"
        }
        .doOnNext {
            it.forEach {
                println(it)
            }
        }
        .fullSubscribe()
}

