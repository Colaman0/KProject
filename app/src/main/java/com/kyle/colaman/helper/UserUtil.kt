package com.kyle.colaman.helper

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.reflect.TypeToken
import com.kyle.colaman.AppContext
import com.kyle.colaman.activity.MainActivity
import com.kyle.colaman.entity.UserInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cookie

/**
 * Author   : kyle
 * Date     : 2020/4/29
 * Function : 用户相关工具类
 */
object UserUtil {
    private val _cookies = mutableListOf<Cookie>()
    private var userInfo: UserInfoEntity? = UserInfoEntity(nickname = "Colaman")

    /**
     * 初始化配置
     *
     */
    fun init() {
        val json = SPUtils.getInstance().getString("user")
        if (json.isNotBlank()) {
            val datas =
                GsonUtils.fromJson<List<Cookie>>(json, object : TypeToken<List<Cookie>>() {}.type)
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

    suspend fun clearCache(context: Context) {
        withContext(Dispatchers.IO) {
            _cookies.clear()
            SPUtils.getInstance().put("user", "")
            PocketRoom.getDatabase(context).pocketDao().clear()
        }
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }


    fun isLogin() = _cookies.isNotEmpty() && userInfo != null

    fun getUserInfo() = userInfo
}