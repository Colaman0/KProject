package com.kyle.colaman.view

import android.view.View
import androidx.annotation.ColorRes
import com.kyle.colman.impl.IStatus
import com.google.android.material.snackbar.Snackbar

/**
 * 实现了IStatus的SnackBar，在对应的状态显示对应的snackBar
 *
 * @param successTips 状态为成功的提示
 * @param failTips 状态为失败的提示
 * @param startTips 状态为开始的提示
 */

class StatusSnackBar(val successConfig: Config? = null,
                     val failConfig: Config? = null,
                     val startConfig: Config? = null) : IStatus {

    override fun success() {
        if (successConfig?.view != null && successConfig.tips.isNotBlank()) {
            val snackbar = Snackbar.make(successConfig.view, successConfig.tips, Snackbar.LENGTH_SHORT)
            if (successConfig.actionTips.isNotBlank()) {
                snackbar.setAction(successConfig.actionTips) { successConfig.callback?.invoke() }
            }
        }
    }

    override fun failed() {
        if (failConfig?.view != null && failConfig.tips.isNotBlank()) {
            val snackbar = Snackbar.make(failConfig.view, failConfig.tips, Snackbar.LENGTH_SHORT)
            if (failConfig.actionTips.isNotBlank()) {
                snackbar.setAction(failConfig.actionTips) { failConfig.callback?.invoke() }
            }
        }
    }

    override fun start() {
        if (startConfig?.view != null && startConfig.tips.isNotBlank()) {
            val snackbar = Snackbar.make(startConfig.view, startConfig.tips, Snackbar.LENGTH_SHORT)
            if (startConfig.actionTips.isNotBlank()) {
                snackbar.setAction(startConfig.actionTips) { startConfig.callback?.invoke() }
            }
        }
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * StatusSnackBar的配置
     * @property view View? 传入一个容器，建议是coordinatorLayout
     * @property tips String 内容文案
     * @property actionTips String 右边按钮的提示文案
     * @property callback Function0<Unit>? 右边按钮的点击回调
     * @property actionColor Int    右边按钮的按钮颜色
     * @constructor
     */
    data class Config(
            val view: View?,
            val tips: String = "",
            val actionTips: String = "",
            val callback: (() -> Unit)? = null,
            @ColorRes val actionColor: Int = 0)
}