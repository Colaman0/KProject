package com.kyle.colman

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOperator
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

/**
 * Author   : kyle
 * Date     : 2020/7/22
 * Function : 缓存
 */
class CacheObservable<T> : Observable<List<T>>() {
    val cacheDatas = mutableListOf<T>()

    fun clearCached() {
        cacheDatas.clear()
    }

    override fun subscribeActual(observer: Observer<in List<T>>?) {
        observer?.onNext(cacheDatas)

        this.subscribe({
            cacheDatas.addAll(it)
        }, {
            observer?.onError(it)
        }, {
            observer?.onComplete()
        })
    }
}

open class CacheOpreat<T> : ObservableOperator<List<T>, List<T>> {
    val cacheDatas = mutableListOf<T>()

    fun clearCached() {
        cacheDatas.clear()
    }

    override fun apply(observer: Observer<in List<T>>?): Observer<in List<T>> {
        if (cacheDatas.isNotEmpty()) {
            observer?.onNext(cacheDatas)
        }
        return object : Observer<List<T>> {
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
        }
    }
}