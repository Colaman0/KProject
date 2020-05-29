package com.kyle.colman.impl

/**
 *
 *     author : kyle
 *     time   : 2019/10/28
 *     desc   : 网络状态变更监听器
 *
 */
interface NetworkStatusListener {
    /**
     * 网络是否正常的回调，以能否上网为准
     * @param available Boolean
     */
    fun onNetworkAvailable(available: Boolean)

    /**
     * 当前网络类型
     * @param type String MOBILE/WIFI
     */
    fun onNetworkType(type: String)

    fun onNetworkChange()

}