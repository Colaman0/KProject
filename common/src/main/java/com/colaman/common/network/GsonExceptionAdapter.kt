package com.colaman.common.common.network

import com.btcpool.common.http.impl.IExceptionAdapter
import com.colaman.common.common.param.KError
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import org.json.JSONException

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : Gson转换相关错误adapter，用于过滤筛选Gson错误
 * </pre>
 */
class GsonExceptionAdapter : IExceptionAdapter<KError> {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is JsonParseException ||
                throwable is JsonIOException ||
                throwable is JSONException
    }

    override fun createException(throwable: Throwable) = KError(throwable, "Json解析错误")
}