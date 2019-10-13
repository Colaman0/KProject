package com.colaman.common.entity

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/9
 *     desc   : 分页实体类基类
 * </pre>
 */
data class PageDTO<T>(val page: Int = 0,
                      val data: T? = null) {

    fun getCurrentPage(): Int {
        return page
    }

    fun getTotalPageSize(): Int {
        return page
    }
}