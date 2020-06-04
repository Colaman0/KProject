package com.kyle.colman.helper

import android.os.SystemClock
import java.util.concurrent.TimeUnit

/**
 * Author   : kyle
 * Date     : 2020/6/4
 * Function : 过滤时间辅助类
 */
class TimeFilterHelper {
    /**
     * 最近一次点击的时间
     */
    private var mLastClickTime: Long = 0

    fun filter(time: Long): Boolean {
        val time = SystemClock.elapsedRealtime()
        val timeInterval = Math.abs(time - mLastClickTime)
        return if (timeInterval < time) {
            true
        } else {
            mLastClickTime = time
            false
        }
    }
}