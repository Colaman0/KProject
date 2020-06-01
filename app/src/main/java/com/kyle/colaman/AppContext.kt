package com.kyle.colaman

import com.colaman.kyle.network.OkhttpFactory
import com.colaman.kyle.network.RetrofitFactory
import com.kyle.colman.config.ApiConfig
import com.kyle.colman.view.KApplication

/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function :
 */
class AppContext : KApplication() {
    override fun onCreate() {
        super.onCreate()
        RetrofitFactory.apiConfig = ApiConfig(url = "https://www.wanandroid.com/")
    }
}