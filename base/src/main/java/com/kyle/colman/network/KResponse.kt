package com.kyle.colman.network

import kotlinx.serialization.json.Json

/**
 * Author   : kyle
 * Date     : 2020/6/1
 * Function : repsonse接口
 */
interface KResponse<T> {
    fun success(): Boolean

    fun responseCode(): Int

    fun responseData(): T?

    fun message(): String

    companion object {
        val exceptionFilters = mutableListOf(JsonFilter(), NetworkFilter(), CancelFilter())
    }

}