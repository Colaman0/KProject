package com.kyle.colaman.api

import com.google.gson.annotations.SerializedName

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/9
 *     desc   : 分页实体类基类
 * </pre>
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
) {

    fun getCurrentPage(): Int {
        return curPage
    }

    fun getTotalPageSize(): Int {
        return total
    }
}