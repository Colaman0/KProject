package com.colaman.kproject.api

import com.colaman.kyle.network.BaseApi
import com.colaman.kyle.common.rx.switchApiThread

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
object Api : BaseApi<IApi>() {
    override fun getApiClass() = IApi::class.java

    fun getTab() = getApi().getV2Json().switchApiThread()

}