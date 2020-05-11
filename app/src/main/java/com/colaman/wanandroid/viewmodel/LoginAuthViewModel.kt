package com.colaman.wanandroid.viewmodel

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.colaman.wanandroid.LoginRegisterActivity

/**
 * Author   : kyle
 * Date     : 2020/4/27
 * Function : 登录授权相关
 */
object LoginAuthViewModel {

    /**
     * 初始化
     *
     */
    fun init() {}

    fun toLogin() {
        val lastActivity = ActivityUtils.getActivityList().last()
        lastActivity.startActivity(Intent(lastActivity, LoginRegisterActivity::class.java))
    }
}