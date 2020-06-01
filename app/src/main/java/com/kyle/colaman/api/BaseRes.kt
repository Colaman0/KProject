package com.kyle.colaman.api

import com.google.gson.annotations.SerializedName
import com.kyle.colman.network.KReponse

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : response 基类
 */
class BaseRes<T>(
    @SerializedName("data") var responseData: T? = null,
    @SerializedName("errorCode") var errorCode: Int = 0,
    @SerializedName("errorMsg") var errorMsg: String = ""
) : KReponse<T> {
    override fun success(): Boolean {
        return responseCode() == 0
    }

    override fun responseCode(): Int {
        return errorCode
    }

    override fun responseData(): T? {
        return responseData
    }

    override fun message(): String {
        return errorMsg
    }
}