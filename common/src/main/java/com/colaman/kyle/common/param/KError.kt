package com.colaman.kyle.common.param

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 作为一个error的载体，可以用于网络请求的错误处理封装，判断错误类型之后对messge等信息做一个客制化
 *              也可以根据errorType做错误类型判断
 * </pre>
 */
open class KError(
    val kThrowable: Throwable,
    val kMessage: String = kThrowable.message.toString(),
    val kCause: String = kThrowable.cause.toString(),
    val errorType: Error = Error.UNKNOW,
    val kTips: String = ""
) : Throwable()

