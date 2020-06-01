package com.kyle.colman.network

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function : 网络请求响应体实现接口
 */

interface KResponse<T> {
    fun success(): Boolean
    fun responseCode(): Int
    fun responseData(): T?
    fun onChange(data: T?)
}