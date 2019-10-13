package com.colaman.common.common.network

import com.colaman.common.imp.IKResponse

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 网络请求response的基类
 * </pre>
 */
class KResponse<T>(var datas: T) : IKResponse<T> {

    override fun isSuccess(): Boolean {
        return true
    }

    override fun isFailed(): Boolean {
        return true

    }

    override fun getResponseCode(): String {
        return ""

    }

    override fun getData(): T {
        return datas

    }

    override fun getMessage(): String {
        return ""

    }

    override fun isAuthorztionSuccess(): Boolean {
        return true
    }

}