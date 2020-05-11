package com.colaman.wanandroid.api

import com.colaman.kyle.common.rx.analysisResponse
import com.colaman.kyle.common.rx.switchApiThread
import com.colaman.kyle.network.BaseApi
import com.colaman.wanandroid.util.UserUtil

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
object Api : BaseApi<IApi>() {
    override fun getApiClass() = IApi::class.java


    fun login(account: String, password: String) = getApi().login(account, password).switchApiThread()
            .analysisResponse()


    fun register(account: String, password: String, rePassword: String) = getApi().register(account, password, rePassword).switchApiThread()
            .analysisResponse()

    fun logout() = getApi().logout().switchApiThread()
            .doOnNext {
                UserUtil.clearCache()
            }


}