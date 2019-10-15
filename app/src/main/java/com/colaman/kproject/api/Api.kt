package com.colaman.kproject.api

import android.database.Observable
import com.colaman.common.common.network.BaseApi
import com.colaman.common.common.rx.switchApiThread

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : api
 *
 */
object Api : BaseApi<IApi>() {
    override fun getApiClass() = IApi::class.java

    fun getTab() = getApi().getTab("apple").switchApiThread()

}