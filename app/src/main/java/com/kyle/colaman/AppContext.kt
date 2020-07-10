package com.kyle.colaman

import androidx.appcompat.app.AppCompatDelegate
import com.blankj.utilcode.util.SPUtils
import com.kyle.colaman.activity.LoginFilter
import com.kyle.colaman.api.UserCookie
import com.kyle.colaman.helper.UserUtil
import com.kyle.colaman.network.RetrofitFactory
import com.kyle.colman.config.ApiConfig
import com.kyle.colman.config.StatusConfig
import com.kyle.colman.network.KResponse
import com.kyle.colman.view.KApplication
import com.kyle.colman.view.StatusLayout
import okhttp3.logging.HttpLoggingInterceptor


/**
 * Author   : kyle
 * Date     : 2020/5/30
 * Function :
 */
class AppContext : KApplication() {

    override fun onCreate() {
        super.onCreate()
        if (SPUtils.getInstance().getBoolean("night", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        UserUtil.init()
        RetrofitFactory.apiConfig = ApiConfig(
            url = "https://www.wanandroid.com/",
            cookieJar = UserCookie,
            interceptors = mutableListOf(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        )
        KResponse.exceptionFilters.add(LoginFilter)
        StatusLayout.setGlobalData(
            StatusConfig(
                StatusLayout.STATUS_LOADING,
                R.layout.layout_loading
            )
        )
        StatusLayout.setGlobalData(
            StatusConfig(
                StatusLayout.STATUS_ERROR,
                R.layout.layout_paging_error,
                clickRes = R.id.btn_reload
            ),
            StatusConfig(
                StatusLayout.STATUS_EMPTY,
                R.layout.layout_empty,
                clickRes = R.id.btn_reload
            )
        )
    }
}