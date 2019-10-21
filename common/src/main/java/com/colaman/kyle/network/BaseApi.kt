package com.colaman.kyle.common.network

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : retrofit 网络请求model的基类
 * </pre>
 */
abstract class BaseApi<T> {
    private val apiManager: T by lazy {
        getApiAchieve()
    }

    /**
     * 获取一个apimanager用于普通的请求
     * @return T
     */
    open fun getApi() = apiManager

    /**
     * 获取具体的实现
     * @return T
     */
    private fun getApiAchieve(): T {
        return RetrofitFactory.create(getApiClass(), getApiBaseUrl())
    }

    /**
     * 返回retrofit接口的class
     * @return Class<T>
     */
    abstract fun getApiClass(): Class<T>

    /**
     * baseurl不同于全局baseurl时重写这个方法
     * @return String
     */
    open fun getApiBaseUrl() = RetrofitFactory.baseurl

    /**
     * 根据baseurl和接口获取一个新的apimanager
     * @param ofclass Class<C>
     * @param url String
     * @return C
     */
    fun <C> getApiManagerByUrl(ofclass: Class<C>, url: String): C {
        return RetrofitFactory.create(ofclass, url)
    }
}