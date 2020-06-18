package com.kyle.colaman.api

import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.entity.UserInfoEntity
import com.kyle.colaman.entity.PageDTO
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Author   : kyle
 * Date     : 2019/10/14
 * Function : api接口
 */
interface IApi {
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") account: String,
        @Field("password") password: String
    ): BaseRes<UserInfoEntity>

    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") account: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseRes<UserInfoEntity>


    @GET("/user/logout/json")
    suspend fun logout(): Unit


    /**
     * 获取首页文章列表
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(@Path("page") page: Int): BaseRes<PageDTO<ArticleEntity>>

    /**
     * 获取首页置顶文章列表
     */
    @GET("article/top/json")
    suspend fun getHomeTopArticles(): BaseRes<MutableList<ArticleEntity>>


    @GET("article/listproject/{page}/json")
    suspend fun getProjects(@Path("page") page: Int): BaseRes<PageDTO<ArticleEntity>>


    @GET("user_article/list/{page}/json")
    suspend fun getGuangchangArticles(@Path("page") page: Int): BaseRes<PageDTO<ArticleEntity>>

    @GET("wenda/list/{page}/json")
    suspend fun getWenda(@Path("page") page: Int): BaseRes<PageDTO<ArticleEntity>>

    @GET("tree/json")
    suspend fun getTixi(): BaseRes<List<TixiEntity>>

    @GET("article/list/{page}/json")
    suspend fun getTixiArticles(
        @Path("page") page: Int,
        @Query("cid") id: Int
    ): BaseRes<PageDTO<ArticleEntity>>

    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id") id: String): BaseRes<Nothing>


    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id") id: String): BaseRes<Nothing>

}