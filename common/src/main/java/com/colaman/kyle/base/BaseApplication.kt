package com.colaman.kyle.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.LogUtils
import com.colaman.kyle.common.brocast.NetworkManager
import io.reactivex.plugins.RxJavaPlugins


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
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun getAppContext() = context
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        catchRxjavaError()
    }

    fun listenNetwork() {
        registerReceiver(
            NetworkManager, IntentFilter()
                .apply {
                    addAction(ConnectivityManager.CONNECTIVITY_ACTION)
                })
    }

    /**
     * 捕获rxjava某些无法传递的error
     * 比如由于下游的生命周期已经达到其终端状态或下游取消了将要发出错误的序列而无法发出的错误。
     */
    fun catchRxjavaError() {
        RxJavaPlugins.setErrorHandler { e ->
            // 在这里可以获取所有错误并且分类，可以重新抛出/选择性上传Error
            LogUtils.e("错误未被rxjava捕获  -> $e")
        }
    }
}
