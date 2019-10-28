package com.colaman.kyle.base

import com.colaman.kyle.impl.IActuator

/**
 * Author   : kyle
 * Date     : 2019/10/28
 * Function : 队列执行器基类
 */
class BaseActuator {

    val actuators by lazy {
        mutableListOf<IActuator>()
    }

    fun push(actuator: IActuator, exeNow: Boolean = false) {
        
    }
}