package com.colaman.kyle.imp

import android.view.View

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/15
 *     desc   : navigationview需要实现的方法，包括选中/未选中状态
 * </pre>
 */
interface INavigationView {
    var select: Boolean


    fun onViewSelect() {
        select = true
    }

    fun onViewUnSelect() {
        select = false
    }

    fun getView(): View?
}