package com.kyle.colaman

import com.kyle.colman.helper.BindableStatus
import com.kyle.colman.helper.IBindStatus
import com.kyle.colman.helper.fullSub
import com.kyle.colman.network.ApiException
import io.reactivex.subjects.PublishSubject

/**
 * Author   : kyle
 * Date     : 2020/6/30
 * Function : test
 */

fun main() {

    val a = PublishSubject.create<Int>()

    a
        .doOnNext {
            println("value = $it")

        }
        .doOnError { println(it) }
        .fullSub(statusList = listOf(B()))

    a.onNext(1)
    a.onNext(2)
    a.onNext(3)
}

class B : IBindStatus {
    override fun onStatus(status: BindableStatus) {
        println("status = $status")
    }

}

