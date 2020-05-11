package com.colaman.kyle.network

import com.colaman.kyle.impl.IKResponse
import com.google.gson.annotations.SerializedName

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 网络请求response的基类
 * </pre>
 */
data class KResponse<T>(@SerializedName("data") var responseData: T? = null,
                        @SerializedName("errorCode") var errorCode: Int = 0,
                        @SerializedName("errorMsg") var errorMsg: String = ""
) : IKResponse<T> {


    override fun isSuccess(): Boolean {
        return errorCode == 0
    }

    override fun isFailed(): Boolean {
        return errorCode != 0
    }

    override fun getResponseCode(): Int {
        return errorCode

    }

    override fun getData(): T? {
        return responseData
    }

    override fun getMessage(): String {
        return errorMsg

    }

    override fun authFail(): Boolean {
        return errorCode == -1001
    }

}