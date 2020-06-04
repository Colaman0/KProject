package com.colaman.wanandroid.api

import com.kyle.colaman.api.IApi
import com.kyle.colman.network.BaseApi

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
object Api : BaseApi<IApi>() {
    override fun getApiClass() = IApi::class.java

    //
//    fun login(account: String, password: String) = getApi().login(account, password).switchApiThread()
//            .analysisResponse()
//
//
//    fun register(account: String, password: String, rePassword: String) = getApi().register(account, password, rePassword).switchApiThread()
//            .analysisResponse()
//
//    fun logout() = getApi().logout().switchApiThread()
//            .doOnNext {
//                UserUtil.clearCache()
//            }
//
//
    suspend fun getHomeArticles(page: Int) = getApi().getHomeArticles(page = page)
//
//    fun getProjects(page: Int) =
//            getApi().getProjects(page = page)
//                    .switchApiThread()
//                    .analysisResponse()
//
//
//    fun getHomeTopArticles(): Observable<MutableList<ArticleEntity>> {
//        return getApi().getHomeTopArticles()
//                .switchApiThread()
//                .analysisResponse()
//    }
//
//    fun getGuangchangArticles(page: Int) =
//            getApi().getGuangchangArticles(page = page)
//                    .switchApiThread()
//                    .analysisResponse()
//
//
//    fun getWenda(page: Int) =
//            getApi().getWenda(page = page)
//                    .switchApiThread()
//                    .analysisResponse()
//
//    fun getTixi() = getApi().getTixi().switchApiThread().analysisResponse()
//
//    fun getTixiArticle(page: Int, id: String) =
//            getApi().getTixiArticles(page, id)
//                    .switchApiThread()
//                    .analysisResponse()
//
//    fun collectArticle(id: Int) = getApi().collectArticle(id.toString()).switchApiThread().analysisResponse()
//
//    fun unCollectArticle(id: Int) = getApi().unCollectArticle(id.toString()).switchApiThread().analysisResponse()


}