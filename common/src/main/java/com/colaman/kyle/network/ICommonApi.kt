package com.colaman.kyle.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 *     author : kyle
 *     time   : 2019/10/25
 *     desc   : 通用的一些api接口
 *
 */
interface ICommonApi {

    @Streaming
    @GET
    fun downloadApk(@Url url: String): Observable<ResponseBody>
}