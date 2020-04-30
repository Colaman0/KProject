package com.colaman.kyle.common.network

import com.colaman.kyle.common.param.KError
import com.colaman.kyle.impl.IExceptionAdapter
import com.colaman.kyle.impl.IExceptionFactory
import com.colaman.kyle.network.GsonExceptionAdapter
import com.colaman.kyle.network.NetworkExceptionAdapter

/**
 * <pre>
 *     author : kyle
 *     time   : 2019-04-22
 *     desc   : 错误处理工厂类
 * </pre>
 */
class KErrorExceptionFactory : IExceptionFactory<KError> {

    companion object {
        /**
         * 默认添加Gson & 网络错误处理类
         */
        val exceptionFactories = mutableListOf(
                GsonExceptionAdapter(),
                NetworkExceptionAdapter()
        )
    }

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