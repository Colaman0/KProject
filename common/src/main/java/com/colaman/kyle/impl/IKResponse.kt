package com.colaman.kyle.imp

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 网络请求最终response的统一格式，便于统一处理结果以及错误/成功
 * </pre>
 */
interface IKResponse<T> {
    fun isSuccess(): Boolean
    fun isFailed(): Boolean
    fun getResponseCode(): String
    fun getData(): T
    fun getMessage(): String
    fun isAuthorztionSuccess(): Boolean
}