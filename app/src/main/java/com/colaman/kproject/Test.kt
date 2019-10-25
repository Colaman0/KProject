package com.colaman.kproject

import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.common.rx.httpRequest
import io.reactivex.Observable

/**
 *
 *     author : kyle
 *     time   : 2019/10/23
 *     desc   : d
 *
 */

fun main() {

    Observable.just(2)
        .flatMap {
            if (it > 4) {
                return@flatMap Observable.error<Int>(Throwable("error"))
            } else {
                return@flatMap Observable.just(it+1)
            }
        }
        .doOnError {
            print(it.message+1)
        }.flatMap {
            if (it >= 4) {
                return@flatMap Observable.error<Int>(Throwable("error"))
            } else {
                return@flatMap Observable.just(it+1)
            }
        }
        .doOnError {
            print(it.message+2)
        }
        .flatMap {
            if (it >= 4) {
                return@flatMap Observable.error<Int>(Throwable("error"))
            } else {
                return@flatMap Observable.just(it+1)
            }
        }
        .doOnError {
            print(it.message+3)
        }
        .fullSubscribe()
}