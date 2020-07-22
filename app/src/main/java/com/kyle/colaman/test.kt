package com.kyle.colaman

import android.util.Log
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.lang.String
import java.util.concurrent.TimeUnit

/**
 * Author   : kyle
 * Date     : 2020/6/30
 * Function : test
 */

fun main() {
    Flowable
        .merge(
        Flowable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
        Flowable.intervalRange(3, 3, 1, 1, TimeUnit.SECONDS)
    )
        .subscribe({ ele -> println(ele.toString()) })
    Observable.just(1).cache()
}

