package com.kyle.colman.view.bineable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kyle.colman.helper.BindableStatus
import com.kyle.colman.impl.IBindStatus
import com.kyle.colman.view.StatusLayout

/**
 * Author   : kyle
 * Date     : 2020/7/20
 * Function : 可绑定的下拉刷新控件
 */
class BindableRefreshLayout : SwipeRefreshLayout, IBindStatus {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    override fun onStatus(status: BindableStatus) {
        when (status) {
            is BindableStatus.BindableDone -> isRefreshing = false
            is BindableStatus.BindableLoading -> isRefreshing = true
        }
    }
}