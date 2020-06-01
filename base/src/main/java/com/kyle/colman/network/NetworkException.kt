package com.kyle.colman.network

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import kotlinx.serialization.json.JsonUnknownKeyException
import org.json.JSONException
import java.lang.Error
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Exception

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : 网络错误异常
 */

class ApiException(val code: Int, message: String) : Throwable()

sealed class ERROR
class JsonError() : ERROR()
class NetWorkError() : ERROR()
class UnkownError() : ERROR()

data class KError(
    val kThrowable: Throwable,
    val kMessage: String = kThrowable.message.toString(),
    val errorType: ERROR = UnkownError(),
    val kTips: String = ""
)

interface IExceptionFilter {
    fun isCreate(throwable: Throwable): Boolean

    fun createKError(throwable: Throwable): KError
}

class JsonFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is JsonParseException ||
                throwable is JsonIOException ||
                throwable is JSONException ||
                throwable is JsonUnknownKeyException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "Json解析错误", errorType = JsonError())
    }

}

class NetworkFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
                throwable is SocketTimeoutException ||
                throwable is ConnectException
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, kTips = "网络异常", errorType = NetWorkError())
    }
}
