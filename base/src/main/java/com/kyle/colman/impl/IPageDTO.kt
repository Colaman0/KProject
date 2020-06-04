package com.kyle.colman.impl

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/9
 *     desc   : 分页实体类基类
 * </pre>
 */
open interface IPageDTO<T> {
    fun currentPage(): Int

    fun nextApiPage(): Int

    fun pageData(): List<T>

    fun isLastPage(): Boolean

    fun isFirstPage(): Boolean

    fun firstPageNum(): Int
}