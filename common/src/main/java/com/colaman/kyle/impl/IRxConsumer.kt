package com.colaman.kyle.imp

/**
 * author : kyle
 * time   : 2019/3/2
 * desc : 为了把observable的subscribe里的方法能够更灵活的去处理 [com.colaman.kyle.common.rx.fullSubscribe]
 * 里会直接回调[IRxConsumer]集合对应的方法，所以我们可以通过 [com.colaman.kyle.common.rx.addConsumerReceiver]
 * 去往集合里添加，把数据分发出去，然后可以做一些对应的操作，
 * 比如[IStatus]和[com.colaman.kyle.common.rx.bindStatusImpl]的转换
 */
interface IRxConsumer<T> {
    /**
     * 四个方法都是给网络请求返回的observable调用
     *
     * @param t
     */
    fun onNext(t: T)

    fun onError(throwable: Throwable)

    fun onComplete()

    fun onUnsucrible()

    fun onSuscrible()

    fun onFinally()
}
