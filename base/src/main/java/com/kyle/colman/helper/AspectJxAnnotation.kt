package com.kyle.colman.helper

/**
 * Author   : kyle
 * Date     : 2020/6/4
 * Function : aop注解类
 */

@Target(AnnotationTarget.FUNCTION)
annotation class FilterTime(
    /**
     * 点击间隔时间
     */
    val value: Long = 1000)

@Target(AnnotationTarget.FUNCTION)
annotation class Debounce(
    /**
     * 点击间隔时间
     */
    val value: Long = 1000)