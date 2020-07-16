package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import com.kyle.colman.impl.IPageSource

/**
 * Author   : kyle
 * Date     : 2020/7/16
 * Function : 分页adapter
 */

class PageAdapter(context: Context) : CommonAdapter(context = context) {
    lateinit var source: IPageSource

    fun bindPageSource(source: IPageSource) {
        this.source = source
    }

    override fun onLoadmore() {
        super.onLoadmore()
        source.loadNextPage()
    }
}