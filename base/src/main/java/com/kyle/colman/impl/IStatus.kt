package com.kyle.colman.impl

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/27
 *     desc   : 用来限制view或者一些其他类似状态管理类的行为，实现这个接口，在对应方法里做操作
 *     for example: 用一个类实现接口，在对应方法里做dialog的显示和隐藏，在网络请求中可以绑定一个IStatus，然后统一处理方法的调用
 *     这样就不用手动去调用dialog的隐藏/dismiss
 * </pre>
 */
interface IStatus {
    fun success()

    fun failed()

    fun start()

    fun destroy()
}