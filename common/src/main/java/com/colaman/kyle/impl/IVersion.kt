package com.colaman.kyle.impl

/**
 *
 *     author : kyle
 *     time   : 2019/10/25
 *     desc   : 检查版本的reponse entity需要实现
 *
 */
interface IVersion {
    /**
     * 由entity判断是否需要更新
     * @return Boolean
     */
    fun needUpdate(): Boolean

    /**
     * 新版apk下载地址
     * @return String
     */
    fun apkUrl(): String

    /**
     * apk名字
     * @return String
     */
    fun fileName(): String
}