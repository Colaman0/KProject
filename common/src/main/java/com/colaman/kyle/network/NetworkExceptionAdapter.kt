package com.colaman.kyle.network

import com.colaman.kyle.impl.IExceptionAdapter
import com.colaman.kyle.common.param.KError
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 网络相关错误adapter，用于过滤筛选网络错误
 * </pre>
 */
class NetworkExceptionAdapter : IExceptionAdapter<KError> {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException
    }

    override fun createException(throwable: Throwable) = KError(throwable, "网络错误")
}