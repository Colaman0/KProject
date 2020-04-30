package com.colaman.wanandroid.util

import com.colaman.kyle.common.param.Error
import com.colaman.kyle.common.param.KError
import com.colaman.kyle.impl.IExceptionAdapter

/**
 * Author   : kyle
 * Date     : 2020/4/30
 * Function : 用户授权登录错误处理类
 */
class UserErrorAdapter : IExceptionAdapter<KError> {
    override fun isCreate(throwable: Throwable) =
            throwable is KError && throwable.errorType == Error.LOGIN

    override fun createException(throwable: Throwable) = throwable as KError
}