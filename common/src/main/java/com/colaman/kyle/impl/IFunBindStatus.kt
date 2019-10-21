package com.colaman.kyle.imp

import com.colaman.kyle.impl.IStatus

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/27
 *     desc   : 绑定IStatus的接口
 * </pre>
 */
interface IFunBindStatus {

    fun bindStatus(vararg status: IStatus)

}