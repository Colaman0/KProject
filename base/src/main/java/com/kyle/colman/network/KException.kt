package com.kyle.colman.network

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.serialization.json.JsonUnknownKeyException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : 错误异常
 */

/**
 * 当api请求出错的时候抛出这个异常代表是请求问题
 *
 * @property code api请求的错误码，可以直接用后台返回的
 * @param message 错误提示描述
 */
class ApiException(val code: Int, message: String) : Throwable(message = message)

/**
 * 当api请求的data是null时抛出的错误，如果[com.kyle.colman.helper.toData]解析实体类的时候允许为null则不会抛出
 *
 */
class DataNullException() : Throwable(message = "网络请求返回数据为空")


/**
 * 通用封装的Error类
 *
 * @property kThrowable
 * @property kMessage
 * @property KErrorTypeType
 * @property kTips
 */
data class KError(
    val kThrowable: Throwable,
    val kMessage: String = kThrowable.message.toString(),
    val KErrorType: KErrorType,
    val kTips: String = ""
) : Throwable(kMessage, kThrowable)

/**
 * 过滤错误的接口，实现这个接口并且添加到[KResponse.exceptionFilters]
 * 调用[com.kyle.colman.helper.toKError]方法可以把异常转换成统一的[KError]
 */
interface IExceptionFilter {
    fun isCreate(throwable: Throwable): Boolean

    fun createKError(throwable: Throwable): KError

    fun onCatch()
}

/**
 * 过滤Json解析的错误
 *
 */
class JsonFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is JsonParseException ||
                throwable is JsonIOException ||
                throwable is JSONException ||
                throwable is JsonUnknownKeyException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "Json解析错误", KErrorType = KErrorType.Json)
    }

    override fun onCatch() {

    }
}

/**
 * 过滤网络错误
 *
 */
class NetworkFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException ||
                throwable is HttpException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "网络异常", KErrorType = KErrorType.NetWork)
    }

    override fun onCatch() {

    }
}

/**
 * 过滤协程取消异常
 *
 */
class CancelFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is CancellationException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "任务取消", KErrorType = KErrorType.Cancel)
    }

    override fun onCatch() {
    }
}

class TimeOutFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is TimeoutException ||
                throwable is TimeoutCancellationException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "超时异常", KErrorType = KErrorType.TimeOut)
    }

    override fun onCatch() {
    }
}


