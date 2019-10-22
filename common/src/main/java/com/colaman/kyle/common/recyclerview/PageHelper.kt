package com.colaman.kyle.base.recyclerview

import com.colaman.kyle.common.recyclerview.adapter.FeaturesRecyclerViewAdapter

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/9
 *     desc   :
 * </pre>
 */
class PageHelper {
    val isLastPage
        get() = currentPage >= totalPage

    var currentPage = 0
    var totalPage = 0

    var adapter: FeaturesRecyclerViewAdapter? = null


    fun bindAdapter(adapter: FeaturesRecyclerViewAdapter) {
        this@PageHelper.adapter = adapter
    }
}