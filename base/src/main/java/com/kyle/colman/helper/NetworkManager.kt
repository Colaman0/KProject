package com.kyle.colman.helper

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData

/**
 *
 *     author : kyle
 *     time   : 2019/10/28
 *     desc   : 网络状态变化广播接收器
 *
 */
object NetworkManager : BroadcastReceiver() {
    val networkLiveData = MutableLiveData<NetworkInfo>()
    private lateinit var connectivityManager: ConnectivityManager


    @SuppressLint("ObsoleteSdkInt")
    override fun onReceive(context: Context?, intent: Intent?) {
        connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (intent != null) {
            val networkAvailable = networkAvailable(connectivityManager)
            val networkType = getNetworkType(connectivityManager)
            networkLiveData.value = NetworkInfo(
                available = networkAvailable,
                networkType = networkType
            )

        }
    }

    fun pingNetwork(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val p = runtime.exec("ping -c 3 www.baidu.com");
            val ret = p.waitFor();
            Log.i("Avalible", "Process:" + ret);
            return ret == 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断wifi是否可用
     * @param connectivityManager ConnectivityManager
     * @return Boolean
     */
    fun wifiActive(connectivityManager: ConnectivityManager): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) ?: false
        } else {
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }
    }

    /**
     * 当前联网类型
     * @param connectivityManager ConnectivityManager
     * @return String
     */
    fun getNetworkType(connectivityManager: ConnectivityManager): String {
        val activeInfo = connectivityManager.activeNetworkInfo
        if (activeInfo != null && activeInfo.isAvailable) {
            return activeInfo.typeName
        }
        return ""
    }

    /**
     * 判断移动数据是否可用
     * @param connectivityManager ConnectivityManager
     * @return Boolean
     */
    fun mobileActive(connectivityManager: ConnectivityManager): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) ?: false
        } else {
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
        }
    }


    /**
     * 判断能否联网
     * @param connectivityManager ConnectivityManager
     * @return Boolean
     */
    fun networkAvailable(connectivityManager: ConnectivityManager): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                ) ?: false
        } else {
            return pingNetwork()
        }
    }
}

data class NetworkInfo(val available: Boolean, val networkType: String)
