package com.colaman.wanandroid

import com.colaman.kyle.base.BaseApplication
import com.colaman.kyle.common.network.KErrorExceptionFactory
import com.colaman.kyle.network.OkhttpFactory
import com.colaman.wanandroid.api.UserCookie
import com.colaman.wanandroid.util.UserErrorAdapter
import com.colaman.wanandroid.util.UserUtil

/**
 * Author   : kyle
 * Date     : 2020/4/27
 * Function : 自定义application
 */
class AppContext : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        OkhttpFactory.setCookieJar(UserCookie)
        UserUtil.init()
        KErrorExceptionFactory.exceptionFactories.add(0, UserErrorAdapter())
    }
}