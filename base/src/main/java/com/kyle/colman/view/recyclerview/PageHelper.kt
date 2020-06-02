package com.kyle.colaman.common.recyclerview

import com.kyle.colman.view.recyclerview.adapter.KAdapter

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/9
 *     desc   :
 * </pre>
 */
class PageHelper {
    var isLastPage = false

    val isFirstPage
        get() = currentPage == 0

    var currentPage = 0
    var nextPage = 0
    var totalPage = 0

    var adapter: KAdapter? = null

}