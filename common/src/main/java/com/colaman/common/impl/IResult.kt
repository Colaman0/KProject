package com.colaman.common.imp

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/8
 *     desc   : 表示结果的接口
 * </pre>
 */
interface IResult {
    /**
     * 成功
     */
    fun OnSuccess() {
        onEnd()
    }

    /**
     * 失败
     *
     */
    fun OnFailed() {
        onEnd()
    }

    /**
     * ing状态
     *
     */
    fun OnLoading()

    /**
     * 可以用作一些资源回收/一些完结的处理，成功/失败都会回调
     *  比如权限请求中，[OnSuccess]/[OnFailed]可以做成功/失败的提示
     *  [onEnd]里做activity的跳转
     */
    fun onEnd()
}