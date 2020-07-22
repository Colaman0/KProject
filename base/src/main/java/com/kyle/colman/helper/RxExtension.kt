package com.kyle.colman.helper

import androidx.lifecycle.*
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colman.CacheObservable
import com.kyle.colman.CacheOpreat
import com.kyle.colman.impl.IBindStatus
import com.tencent.smtt.utils.s
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromAction
import io.reactivex.rxjava3.core.ObservableOperator
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Author   : kyle
 * Date     : 2020/6/30
 * Function : rx拓展
 */


fun <T> Observable<T>.bindViewModel(viewmodel: BaseViewModel): Observable<T> {
    val source = PublishSubject.create<Boolean>()
    viewmodel.addClearCallback {
        source.onNext(true)
    }


    return takeUntil(source)
}

fun <T> Observable<T>.bindLifeCycle(lifecycle: Lifecycle): Observable<T> {
    val source = PublishSubject.create<Boolean>()
    val observer = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            source.onNext(true)
            lifecycle.removeObserver(this)
        }
    }
    lifecycle.addObserver(observer)
    return takeUntil(source)
}

fun <T> Observable<T>.fullSub(
    statusList: List<IBindStatus> = listOf()
) =
    doFinally {
        statusList.forEach { it.onStatus(BindableStatus.BindableDone) }
    }
        .doOnSubscribe {
            // 开始
            statusList.forEach { it.onStatus(BindableStatus.BindableLoading) }
        }
        .subscribe(
            {
                // 收到数据
                statusList.forEach { it.onStatus(BindableStatus.BindableSuccess) }
            }, { throwable ->
                // 处理错误
                statusList.forEach { it.onStatus(BindableStatus.BindableError(throwable.toKError())) }
            }, {
                // 任务完成
                statusList.forEach { it.onStatus(BindableStatus.BindableSuccessDone) }
            }
        )

fun <T> Observable<T>.cacheHistory() = lift<T>(CacheOpreat<T>() as ObservableOperator<out T, in T>)


sealed class BindableStatus {
    object BindableLoading : BindableStatus()
    object BindableSuccess : BindableStatus()
    class BindableError(throwable: Throwable) : BindableStatus()
    object BindableDone : BindableStatus()
    object BindableSuccessDone : BindableStatus()
}

