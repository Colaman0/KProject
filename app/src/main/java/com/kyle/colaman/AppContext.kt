package com.kyle.colaman

import com.kyle.colaman.network.OkhttpFactory
import com.kyle.colaman.network.RetrofitFactory
import com.kyle.colaman.activity.LoginFilter
import com.kyle.colaman.api.UserCookie
import com.kyle.colaman.helper.UserUtil
import com.kyle.colman.config.ApiConfig
import com.kyle.colman.network.KReponse
import com.kyle.colman.view.KApplication
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function :
 */
class AppContext : KApplication() {
    override fun onCreate() {
        super.onCreate()
        UserUtil.init()
        RetrofitFactory.apiConfig = ApiConfig(
            url = "https://www.wanandroid.com/",
            cookieJar = UserCookie,
            interceptors = mutableListOf(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        )
        KReponse.exceptionFilters.add(LoginFilter)
    }
}