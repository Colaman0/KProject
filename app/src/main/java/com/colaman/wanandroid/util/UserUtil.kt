package com.colaman.wanandroid.util

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.colaman.wanandroid.entity.UserInfoEntity
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie

/**
 * Author   : kyle
 * Date     : 2020/4/29
 * Function : 用户相关工具类
 */
object UserUtil {
    private val _cookies = mutableListOf<Cookie>()
    private var userInfo: UserInfoEntity? = null

    /**
     * 初始化配置
     *
     */
    fun init() {
        val json = SPUtils.getInstance().getString("user")
        if (json.isNotBlank()) {
            val datas = GsonUtils.fromJson<List<Cookie>>(json, object : TypeToken<List<Cookie>>() {}.type)
            if (!datas.isNullOrEmpty()) {
                _cookies.addAll(datas as MutableList<Cookie>)
            }
        }
    }

    /**
     * 返回登录成功之后的cookie
     *
     */
    fun getUserCookie() = _cookies

    /**
     * 设置登录之后返回的cookie
     *
     * @param cookies
     */
    fun updateUserCookie(cookies: List<Cookie>) {
        _cookies.clear()
        _cookies.addAll(cookies)
        SPUtils.getInstance().put("user", GsonUtils.toJson(cookies))
    }

    fun clearCache() {
        _cookies.clear()
        SPUtils.getInstance().put("user", "")
    }


    fun isLogin() = _cookies.isNotEmpty() && userInfo != null

    fun getUserInfo() = userInfo
}