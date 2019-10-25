package com.colaman.kyle.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : okhttp构建类
 * </pre>
 */
object OkhttpFactory {
    private const val mDefaultConnectTimeOut = 10L
    private const val mDefaultReadTimeOut = 10L
    private const val mDefaultWriteTimeOut = 10L
    private const val mDefaultRetryOnConnectionFailure = true
    private val mDefaultInterceptors = mutableListOf<Interceptor>(
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    )
    private val mDefaultNetworkInterceptors = mutableListOf<Interceptor>()

    /**
     * 获取一个okhttpclient
     *
     * @param connectTimeOut 连接超时时间
     * @param writeTimeOut  写入超时时间
     * @param readTimeOut   读取超时时间
     * @param retryOnConnectionFailure  是否进行错误重试
     * @param interceptor 应用拦截器list
     * @param networkInterceptor  network拦截器list
     * @return
     */
    fun getHttpClient(
        connectTimeOut: Long = mDefaultConnectTimeOut,
        writeTimeOut: Long = mDefaultWriteTimeOut,
        readTimeOut: Long = mDefaultReadTimeOut,
        retryOnConnectionFailure: Boolean = mDefaultRetryOnConnectionFailure,
        interceptor: MutableList<Interceptor> = mDefaultInterceptors,
        networkInterceptor: MutableList<Interceptor> = mDefaultNetworkInterceptors
    ): OkHttpClient {
        val builder = getHttpClientBuilder(
            connectTimeOut,
            writeTimeOut,
            readTimeOut,
            retryOnConnectionFailure
        )
        /**
         * 添加应用拦截器
         */
        interceptor.forEach {
            builder.addInterceptor(it)
        }
        /**
         * 添加network拦截器
         */
        networkInterceptor.forEach {
            builder.addNetworkInterceptor(it)
        }
        return builder.build()
    }


    /**
     * 获取okhttpclientbuilder
     *
     * @param connectTimeOut 连接超时时间
     * @param writeTimeOut  写入超时时间
     * @param readTimeOut   读取超时时间
     * @param retryOnConnectionFailure  是否进行错误重试
     */
    fun getHttpClientBuilder(
        connectTimeOut: Long = mDefaultConnectTimeOut,
        writeTimeOut: Long = mDefaultWriteTimeOut,
        readTimeOut: Long = mDefaultReadTimeOut,
        retryOnConnectionFailure: Boolean = mDefaultRetryOnConnectionFailure
    ) =
        OkHttpClient.Builder()
            .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
            .readTimeout(readTimeOut, TimeUnit.SECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retryOnConnectionFailure)
            .toUnsafe()


    /**
     * 添加一个拦截器，并且返回一个list，list中包含默认的拦截器以及添加进来的拦截器
     *
     * @param interceptor
     * @return
     */
    fun addInterceptor(interceptor: Interceptor): MutableList<Interceptor> {
        val interceptors = mutableListOf(interceptor)
        interceptors.addAll(mDefaultInterceptors)
        return interceptors
    }

    /**
     * 添加一个拦截器，并且返回一个list，list中包含默认的拦截器以及添加进来的拦截器
     *
     * @param interceptor
     * @return
     */
    fun addNetWorkInterceptor(interceptor: Interceptor): MutableList<Interceptor> {
        val interceptors = mutableListOf(interceptor)
        interceptors.addAll(mDefaultNetworkInterceptors)
        return interceptors
    }

}