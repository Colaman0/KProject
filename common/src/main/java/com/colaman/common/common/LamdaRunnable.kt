package com.colaman.common.common

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/19
 *     desc   : 存放一个lamda函数
 * </pre>
 */
class LamdaRunnable(val lamdaRunnable: (() -> Unit)? = null) {

    /**
     * 执行lamda函数
     */
    fun start() {
        lamdaRunnable?.invoke()
    }
}