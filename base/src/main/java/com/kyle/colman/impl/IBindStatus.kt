package com.kyle.colman.impl

import com.kyle.colman.helper.BindableStatus

/**
 * Author   : kyle
 * Date     : 2020/7/20
 * Function : 可绑定的接口，表示这个view可以接收协程/rxjava流状态
 */
interface IBindStatus {
    fun onStatus(status: BindableStatus)
}