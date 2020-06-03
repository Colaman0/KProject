package com.kyle.colman.network

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : 错误密封类
 */

open class ERROR

object JsonError : ERROR()
object NetWorkError : ERROR()
object UnknownError : ERROR()
object Cancel : ERROR()
