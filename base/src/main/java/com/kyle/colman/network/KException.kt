package com.kyle.colman.network

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import kotlinx.serialization.json.JsonUnknownKeyException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

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
 * @property errorType
 * @property kTips
 */
data class KError(
    val kThrowable: Throwable,
    val kMessage: String = kThrowable.message.toString(),
    val errorType: ERROR = UnknownError,
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
        return KError(throwable, kTips = "Json解析错误", errorType = JsonError)
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
        return KError(throwable, kTips = "网络异常", errorType = NetWorkError)
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
        return KError(throwable, kTips = "任务取消", errorType = Cancel)
    }

    override fun onCatch() {
    }
}

