package com.colaman.wanandroid.api

import com.colaman.wanandroid.util.UserUtil
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


/**
 * Author   : kyle
 * Date     : 2020/4/28
 * Function : user cookie保存管理
 */
object UserCookie : CookieJar {


    override fun loadForRequest(url: HttpUrl): List<Cookie> {

        return UserUtil.getUserCookie()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toUrl().file == "/user/login") {
            UserUtil.updateUserCookie(cookies)
        }
    }
}