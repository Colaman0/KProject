package com.kyle.colman.config
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * Create by kyle on 2018/10/17
 * Function : 状态属性
 */
data class StatusConfig(
        var status: String,
        @field:LayoutRes
        var layoutRes: Int = 0,
        var view: View? = null,
        @field:IdRes
        var clickRes: Int = 0,
        var autoClcik: Boolean = true)
