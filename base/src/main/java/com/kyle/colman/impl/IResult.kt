package com.kyle.colman.impl

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/8
 *     desc   : 表示结果的接口
 * </pre>
 */
sealed class RESULT{
    object SUCCESS : RESULT()
    object FAILED : RESULT()
    object LOGINDG : RESULT()
    object COMPLETED : RESULT()
}


interface IResult {
    val state: RESULT
}