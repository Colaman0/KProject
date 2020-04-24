package com.colaman.kyle.common.helper

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.entity.Constants
import com.colaman.kyle.impl.IBackpressInterceptor
import kotlin.system.exitProcess

/**
 *
 *     author : kyle
 *     time   : 2019/10/21
 *     desc   : 双击退出应用的拦截器
 *
 */
class DoubleClickExitInterceptor : IBackpressInterceptor {
    var lastClick = 0L

    override fun OnInterceptor(activity: BaseActivity<*,*>): Boolean {
        /**
         * 当app只有一个activity的时候，触发双击退出应用, 多于一个的时候正常
         */
        if (ActivityUtils.getActivityList().size <= 1) {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastClick < Constants.DOUBLE_CLICK_TIME) {
                activity.finish()
                exitProcess(0)
            } else {
                ToastUtils.showShort("再按一次退出应用")
                lastClick = currentTimestamp
                return true
            }
        }
        return false
    }


}