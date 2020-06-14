package com.colaman.wanandroid.api

import com.kyle.colaman.api.IApi
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.PageDTO
import com.kyle.colaman.helper.UserUtil
import com.kyle.colman.helper.io
import com.kyle.colman.helper.toData
import com.kyle.colman.network.BaseApi
import kotlinx.coroutines.flow.flow

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
object Api : BaseApi<IApi>() {
    override fun getApiClass() = IApi::class.java


    suspend fun login(account: String, password: String) =
        getApi().login(account, password).toData()


    suspend fun register(account: String, password: String, rePassword: String) =
        getApi().register(account, password, rePassword).toData()

    suspend fun logout(): Boolean {
        return try {
            getApi().logout()
            UserUtil.clearCache()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getHomeArticles(page: Int): PageDTO<ArticleEntity> {
        return getApi().getHomeArticles(page = page).toData()!!
    }

    suspend fun getProjects(page: Int) =
        getApi().getProjects(page = page).toData()


    suspend fun getHomeTopArticles(): MutableList<ArticleEntity> {
        return getApi().getHomeTopArticles().toData() ?: mutableListOf()
    }

    suspend fun getGuangchangArticles(page: Int) =
        getApi().getGuangchangArticles(page = page).toData()

    suspend fun test() = flow {
        emit(getApi().collectArticle("1--").toData()!!)
    }.io()

    suspend fun getWenda(page: Int) = getApi().getWenda(page = page).toData()

    suspend fun getTixi() = flow { emit(getApi().getTixi().toData()!!) }.io()

    suspend fun getTixiArticle(page: Int, id: String) =
        getApi().getTixiArticles(page, id).toData()

    suspend fun collectArticle(id: Int) =
        getApi().collectArticle(id.toString()).toData()

    suspend fun unCollectArticle(id: Int) =
        getApi().unCollectArticle(id.toString()).toData()
}