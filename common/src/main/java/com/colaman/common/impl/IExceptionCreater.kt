package com.btcpool.common.http.impl


/**
 * <pre>
 *     author : kyle
 *     time   : 2019-04-22
 *     desc   : exception adapter 接口 ，实现该接口来实现是否拦截某个错误并且把对应错误信息返回
 * </pre>
 */
interface IExceptionAdapter<T> {
    fun isCreate(throwable: Throwable): Boolean

    fun createException(throwable: Throwable): T
}