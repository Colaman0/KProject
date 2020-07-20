package com.kyle.colman.network

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : 错误密封类
 */
sealed class KErrorType {
    object Json : KErrorType()
    object NetWork : KErrorType()
    object Api : KErrorType()
    object Unknown : KErrorType()
    object Cancel : KErrorType()
    object TimeOut : KErrorType()
}

