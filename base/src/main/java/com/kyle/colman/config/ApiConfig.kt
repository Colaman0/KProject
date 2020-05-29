package com.kyle.colman.config

import okhttp3.CookieJar
import okhttp3.Interceptor

/**
 * Author   : kyle
 * Date     : 2020/5/29
 * Function : 网络请求配置
 */

data class ApiConfig(
    val url: String = "",
    val interceptors: MutableList<Interceptor> = mutableListOf(),
    val networkInterceptors: MutableList<Interceptor> = mutableListOf(),
    val cookieJar: CookieJar? = null
)