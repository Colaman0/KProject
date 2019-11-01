package com.colaman.kyle.common.rx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.colaman.kyle.imp.IRxData
import com.colaman.kyle.impl.IStatus
import io.reactivex.Observable

/**
 * <pre>
 * author : kyle
 * time   : 2019/3/2
 * desc   : 实现了IRxData，负责把网络请求的observable数据封装状态，通过livedata发射出去
</pre> *
 */
open class RxLivedata<T> : MutableLiveData<RxData<T>>(), IRxData<T> {
    override fun onFinally() {
    }

    var currentObservable: Observable<T>? = null

    override fun onNext(t: T) {
        super.postValue(RxData(t))
    }

    override fun onError(throwable: Throwable) {
        super.postValue(RxData(throwable))
    }

    override fun onComplete() {
        super.postValue(RxData(RxData.STATUS.COMPLETE))
    }

    override fun onUnsucrible() {
        super.postValue(RxData(RxData.STATUS.UNSUSCRIBE))
    }

    override fun onSuscrible() {
        super.postValue(RxData(RxData.STATUS.SUSCRIBERIB))
    }

    override fun sendData(data: RxData<T>) {
        super.postValue(data)
    }

    fun bindObservable(observable: Observable<T>): RxLivedata<T> {
        currentObservable = observable
        return this
    }

    fun bindStatusImpl(vararg status: IStatus): RxLivedata<T> {
        currentObservable?.bindStatusImpl(*status)
        return this
    }

    fun rxObserve(owner: LifecycleOwner, observer: Observer<in RxData<T>>): RxLivedata<T> {
        super.observe(owner, observer)
        return this
    }


    fun rxObserveForever(observer: Observer<in RxData<T>>): RxLivedata<T> {
        super.observeForever(observer)
        return this
    }
}
