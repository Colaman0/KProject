package com.colaman.kyle.view

import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.impl.IStatus

/**
 * 实现了IStatus的toast，在对应的状态显示对应的文本toast
 *
 * @param successTips 状态为成功的提示
 * @param failTips 状态为失败的提示
 * @param startTips 状态为开始的提示
 */

class StatusToast(val successTips: String = "",
                  val failTips: String = "",
                  val startTips: String = "") : IStatus {
    override fun success() {
        if (successTips.isNotBlank()) {
            ToastUtils.showShort(successTips)
        }
    }

    override fun failed() {
        if (failTips.isNotBlank()) {
            ToastUtils.showShort(failTips)
        }
    }

    override fun start() {
        if (startTips.isNotBlank()) {
            ToastUtils.showShort(startTips)
        }
    }

    override fun destroy() {
    }
}