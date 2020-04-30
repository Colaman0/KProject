package com.colaman.wanandroid.api

import com.colaman.kyle.network.KResponse
import com.colaman.wanandroid.entity.UserInfoEntity
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Author   : kyle
 * Date     : 2019/10/14
 * Function : api接口
 */
interface IApi {
    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("username") account: String,
              @Field("password") password: String): Observable<KResponse<UserInfoEntity>>

    @POST("/user/register")
    @FormUrlEncoded
    fun register(@Field("username") account: String,
                 @Field("password") password: String,
                 @Field("repassword") repassword: String): Observable<KResponse<UserInfoEntity>>


    @GET("/user/logout/json")
    fun logout(): Observable<Unit>
}