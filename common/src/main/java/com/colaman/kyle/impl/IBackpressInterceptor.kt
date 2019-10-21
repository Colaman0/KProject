package com.colaman.kyle.impl

import com.colaman.kyle.base.BaseActivity

/**
 *
 *     author : kyle
 *     time   : 2019/10/21
 *     desc   : activity返回按钮的拦截器接口
 *
 */
interface IBackpressInterceptor {

    /**
     * 返回值决定是否拦截下来事件
     * @param activity BaseActivity<*>
     * @return Boolean
     */
    fun OnInterceptor(activity: BaseActivity<*>): Boolean
}