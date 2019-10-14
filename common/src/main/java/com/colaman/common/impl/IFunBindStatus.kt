package com.colaman.common.imp

import com.colaman.common.impl.IStatus

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