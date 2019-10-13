package com.colaman.common.common.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
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
    /**
     * 默认的baseurl
     */
    const val baseurl: String = ""

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
        baseUrl: String = baseurl,
        okhttpClient: OkHttpClient = OkhttpFactory.getHttpClient(),
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
        baseUrl: String = baseurl,
        okhttpClient: OkHttpClient = OkhttpFactory.getHttpClient(),
        callAdapters: Array<CallAdapter.Factory> = arrayOf(mRxjavaAdapter),
        converterAdapters: Array<Converter.Factory> = arrayOf(mGsonCallAdapter)
    ): Retrofit {
        return getRetrofitClientBuilder(baseUrl, okhttpClient, callAdapters, converterAdapters).build()
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
        return if (url == baseurl) {
            defaultRetrofitClient.create(clazz)
        } else {
            getRetrofitClient(baseUrl = url).create(clazz)
        }
    }
}