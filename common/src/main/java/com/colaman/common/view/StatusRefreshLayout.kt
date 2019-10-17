package com.colaman.common.view

import android.content.Context
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.colaman.common.impl.IStatus

/**
 *
 *     author : kyle
 *     time   : 2019/10/17
 *     desc   : 实现了IStatus接口的下拉刷新refreshlayout
 *
 */
class StatusRefreshLayout(context: Context) : SwipeRefreshLayout(context), IStatus {
    override fun success() {
        isRefreshing = false
    }

    override fun failed() {
    }

    override fun start() {
        isRefreshing = true
    }

    override fun destroy() {
    }

}