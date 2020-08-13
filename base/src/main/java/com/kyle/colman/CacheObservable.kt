package com.kyle.colman

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Author   : kyle
 * Date     : 2020/7/22
 * Function : 缓存
 */


class CacheObservable<T>(val source: ObservableSource<List<T>>) : Observable<List<T>>() {
    val cacheDatas = mutableListOf<T>()

    fun clearCached() {
        cacheDatas.clear()
    }

    override fun subscribeActual(observer: Observer<in List<T>>?) {
        if (cacheDatas.isNotEmpty()) {
            observer?.onNext(cacheDatas)
        }
        source.subscribe(object : Observer<List<T>> {
            override fun onComplete() {
                observer?.onComplete()
            }

            override fun onSubscribe(d: Disposable?) {
                observer?.onSubscribe(d)
            }

            override fun onNext(t: List<T>) {
                cacheDatas.addAll(t)
                observer?.onNext(t)
            }

            override fun onError(e: Throwable?) {
                observer?.onError(e)
            }
        })
    }
}
