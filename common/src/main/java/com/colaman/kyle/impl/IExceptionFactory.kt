package com.colaman.kyle.imp

import com.btcpool.common.http.impl.IExceptionAdapter


/**
 *     author : kyle
 *     time   : 2019-04-22
 *     desc   : 错误工厂类需要实现的接口
 * [analysisExcetpion] 方法传入一个throwable，返回的类型自己定义，比如KError，做一个统一的错误信息处理
 * [addExceptionCreator] 添加一个错误的Creater，把错误信息的拦截以及筛选交给Creater去实现
 */
interface IExceptionFactory<T> {

    fun analysisExcetpion(throwable: Throwable): T

    fun addExceptionCreator(creator: IExceptionAdapter<T>)
}