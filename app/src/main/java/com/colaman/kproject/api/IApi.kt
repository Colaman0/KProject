package com.colaman.kproject.api

import com.colaman.common.common.network.KResponse
import com.colaman.kproject.model.V2Entity
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

/**
 * Author   : kyle
 * Date     : 2019/10/14
 * Function : api接口
 */
interface IApi {
    @GET("api/topics/hot.json")
    fun getV2Json(): Observable<Response<KResponse<V2Entity>>>
}