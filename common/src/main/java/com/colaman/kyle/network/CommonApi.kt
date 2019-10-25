package com.colaman.kyle.network

import io.reactivex.schedulers.Schedulers

/**
 *
 *     author : kyle
 *     time   : 2019/10/25
 *     desc   : 通用api请求
 *
 */
object CommonApi : BaseApi<ICommonApi>() {
    override fun getApiClass() = ICommonApi::class.java

    /**
     * 下载文件
     * @param url String  文件地址
     * @return (io.reactivex.Observable<(okhttp3.ResponseBody..okhttp3.ResponseBody?)>..io.reactivex.Observable<(okhttp3.ResponseBody..okhttp3.ResponseBody?)>?)
     */
    fun download(url: String) = getApi().downloadApk(url).subscribeOn(Schedulers.io())
}