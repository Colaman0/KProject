package com.colaman.kyle.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kyle.colman.config.ApiConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : Retroift构建类
 * </pre>
 */
object RetrofitFactory {

    val apiConfig: ApiConfig? = null

    private val mGsonCallAdapter by lazy {
        GsonConverterFactory.create()
    }

    private val mRxjavaAdapter by lazy {
        RxJava2CallAdapterFactory.create()
    }

    /**
     * 默认配置的retrofitclient
     */
    val defaultRetrofitClient by lazy {
        getRetrofitClient()
    }

    fun getRetrofitClientBuilder(
        baseUrl: String = getBaseUrl(),
        okhttpClient: OkHttpClient = OkhttpFactory.getHttpClient(
            apiConfig = apiConfig!!
        ),
        callAdapters: Array<CallAdapter.Factory> = arrayOf(mRxjavaAdapter),
        converterAdapters: Array<Converter.Factory> = arrayOf(mGsonCallAdapter)
    ): Retrofit.Builder {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient)
        callAdapters.forEach {
            builder.addCallAdapterFactory(it)
        }
        converterAdapters.forEach {
            builder.addConverterFactory(it)
        }
        return builder
    }


    fun getRetrofitClient(
        baseUrl: String = apiConfig?.url ?: "",
        okhttpClient: OkHttpClient = OkhttpFactory.getHttpClient(apiConfig = apiConfig!!),
        callAdapters: Array<CallAdapter.Factory> = arrayOf(mRxjavaAdapter),
        converterAdapters: Array<Converter.Factory> = arrayOf(mGsonCallAdapter)
    ): Retrofit {
        return getRetrofitClientBuilder(
            baseUrl,
            okhttpClient,
            callAdapters,
            converterAdapters
        ).build()
    }

    /**
     * create一个retrofit实例
     *
     * @param T
     * @param clazz 接口类
     * @param url baseurl
     * @return
     */
    fun <T> create(clazz: Class<T>, url: String): T {
        /**z
         * 判断一下baseURL是否一样，如果是的话用默认的client
         */
        return if (url == getBaseUrl()) {
            defaultRetrofitClient.create(clazz)
        } else {
            getRetrofitClient(baseUrl = url).create(clazz)
        }
    }

    fun getBaseUrl() = apiConfig?.url ?: ""

}