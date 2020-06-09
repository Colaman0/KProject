package com.kyle.colman.network

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import kotlinx.serialization.json.JsonUnknownKeyException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : 网络错误异常
 */

class ApiException(val code: Int, message: String) : Throwable(message = message)


data class KError(
    val kThrowable: Throwable,
    val kMessage: String = kThrowable.message.toString(),
    val errorType: ERROR = UnknownError,
    val kTips: String = ""
)

interface IExceptionFilter {
    fun isCreate(throwable: Throwable): Boolean

    fun createKError(throwable: Throwable): KError

    fun onCatch()
}

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

class NetworkFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "网络异常", errorType = NetWorkError)
    }

    override fun onCatch() {

    }
}

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

