package com.colaman.kyle.base

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import com.colaman.kyle.common.brocast.NetworkManager

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/9/18
 *     desc   : 自定义application基类
 * </pre>
 */
open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this
        listenNetwork()
    }

    companion object {
        private var context: Context? = null
        fun getAppContext() = context
    }

    fun listenNetwork() {
        registerReceiver(
            NetworkManager, IntentFilter()
            .apply {
                addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            })
    }
}
