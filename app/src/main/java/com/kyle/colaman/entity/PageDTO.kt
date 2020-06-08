package com.kyle.colaman.entity

import com.google.gson.annotations.SerializedName
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IResult
import com.kyle.colman.impl.RESULT

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : 分页entity
 */
data class PageDTO<T>(
    @SerializedName("curPage")
    var curPage: Int = 0,
    @SerializedName("datas")
    var datas: MutableList<T> = mutableListOf<T>(),
    @SerializedName("offset")
    var offset: Int = 0,
    @SerializedName("over")
    var over: Boolean = true,
    @SerializedName("pageCount")
    var pageCount: Int = 0,
    @SerializedName("size")
    var size: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) : IPageDTO<T> {

    override fun currentPage(): Int {
        return curPage - 1
    }

    override fun pageData(): List<T> {
        return datas
    }

    override fun isLastPage(): Boolean {
        return over
    }

    override fun isFirstPage(): Boolean {
        return currentPage() == 0
    }

    override fun firstPageNum(): Int {
        return 0
    }

    override fun nextApiPage(): Int {
        return curPage
    }
}