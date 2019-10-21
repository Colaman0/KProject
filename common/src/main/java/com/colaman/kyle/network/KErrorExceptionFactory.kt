package com.colaman.kyle.common.network

import com.btcpool.common.http.impl.IExceptionAdapter
import com.colaman.kyle.common.param.KError
import com.colaman.kyle.imp.IExceptionFactory

/**
 * <pre>
 *     author : kyle
 *     time   : 2019-04-22
 *     desc   : 错误处理工厂类
 * </pre>
 */
class KErrorExceptionFactory : IExceptionFactory<KError> {

    /**
     * 默认添加Gson & 网络错误处理类
     */
    private val exceptionFactories = mutableListOf(
            GsonExceptionAdapter(),
            NetworkExceptionAdapter()
    )

    override fun addExceptionCreator(creator: IExceptionAdapter<KError>) {
        exceptionFactories.add(0, creator)
    }

    override fun analysisExcetpion(throwable: Throwable): KError {
        exceptionFactories.forEach {
            if (it.isCreate(throwable)) {
                return it.createException(throwable)
            }
        }
        return KError(throwable)
    }
}