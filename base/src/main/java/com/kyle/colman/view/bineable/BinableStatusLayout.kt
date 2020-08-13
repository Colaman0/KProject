package com.kyle.colman.view.bineable

import android.content.Context
import android.util.AttributeSet
import com.kyle.colman.helper.BindableStatus
import com.kyle.colman.impl.IBindStatus
import com.kyle.colman.view.StatusLayout

/**
 * Author   : kyle
 * Date     : 2020/7/20
 * Function : 可绑定的statuslayout
 */

class BinableStatusLayout(context: Context, attrs: AttributeSet? = null) :
    StatusLayout(context, attrs), IBindStatus {
    override fun onStatus(status: BindableStatus) {
        when (status) {
            is BindableStatus.BindableSuccess -> showDefaultContent()
            is BindableStatus.BindableError -> switchLayout(STATUS_ERROR)
            is BindableStatus.BindableLoading -> switchLayout(STATUS_LOADING)
        }
    }
}