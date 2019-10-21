package com.colaman.kyle.common.rx

import com.colaman.kyle.imp.IRxConsumer
import com.colaman.kyle.impl.IStatus

/**
 * <pre>
 *   author : kyle
 *   time   : 2019/8/6
 *   desc   : 方便[bindStatusImpl]方法调用，只是作为中间层
 * </pre>
 */
class StatusTransformer<T>(val status: IStatus) : IRxConsumer<T> {


    override fun onNext(t: T) {
    }

    override fun onError(throwable: Throwable) {
        status.failed()
    }

    override fun onComplete() {
        status.success()
    }

    override fun onUnsucrible() {
    }

    override fun onSuscrible() {
        status.start()
    }

    override fun onFinally() {
        status.destroy()
    }
}