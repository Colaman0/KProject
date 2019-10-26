package com.colaman.kyle.impl

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : 需要实现next/before功能，实现连接节点
 *
 */
interface INextable<T> {
    fun next(): T?

    fun before(): T?
}