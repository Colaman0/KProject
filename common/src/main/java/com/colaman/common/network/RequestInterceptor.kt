package com.colaman.common.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : 请求拦截器
 *
 */
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(request)
    }
}


object RequestManager {

}