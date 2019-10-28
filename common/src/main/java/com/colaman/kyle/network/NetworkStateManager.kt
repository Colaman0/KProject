package com.colaman.kyle.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest

/**
 *
 *     author : kyle
 *     time   : 2019/10/28
 *     desc   : 网络状态监听
 *
 */
object NetworkStateManager {

    fun receive(context: Context) {
        val connectManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }
}