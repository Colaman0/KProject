package com.kyle.colman.helper

import android.os.SystemClock
import io.reactivex.internal.operators.flowable.FlowableDistinct
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

    /**
     * 判断要不要过滤掉这次事件，true为忽略，false为执行
     */
    fun filter(distinct: Long): Boolean {
        val time = SystemClock.elapsedRealtime()
        val timeInterval = Math.abs(time - mLastClickTime)
        return if (timeInterval < distinct) {
            true
        } else {
            mLastClickTime = time
            false
        }
    }
}