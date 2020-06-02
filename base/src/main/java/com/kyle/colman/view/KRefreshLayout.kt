package com.kyle.colaman.view

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kyle.colman.view.RefreshCallback
import com.kyle.colman.view.RefreshView

/**
 * Author   : kyle
 * Date     : 2020/5/11
 * Function : 下拉刷新
 */
class KRefreshLayout : SwipeRefreshLayout, RefreshView {
    val actions = mutableListOf<RefreshCallback>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        setOnRefreshListener {
            actions.forEach { it.refresh() }
        }
    }

    override fun addRefreshFun(callback: RefreshCallback) {
        actions.add(callback)
    }

    override fun stopRefresh() {
        isRefreshing = false
        actions.forEach { it.stopRefresh() }
    }

    override fun startRefresh() {
        isRefreshing = true
        actions.forEach { it.refresh() }
    }

    override fun clearFuns() {
        actions.clear()
    }

    override fun disableRefresh(disable: Boolean) {
        isEnabled = disable
    }

    override fun isRefreshing(): Boolean {
        return super.isRefreshing()
    }
}