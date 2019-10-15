package com.colaman.kproject.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
interface IApi {
    @GET(".")
    fun getTab(@Query("tab") tab: String): Observable<String>
}