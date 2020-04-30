package com.colaman.kyle.common.helper

import android.content.Intent
import android.provider.Settings
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils
import com.colaman.kyle.R
import com.colaman.kyle.base.BaseApplication
import com.colaman.kyle.network.NetworkStatusListener
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 * Author   : kyle
 * Date     : 2019/10/28
 * Function : 网络状态view提醒辅助类
 */
object NetworkViewManager : NetworkStatusListener {

    override fun onNetworkAvailable(available: Boolean) {
        /**
         * 没有网络的时候弹出一个snackbar提醒用户
         */
        if (!available && ActivityUtils.getTopActivity() != null) {
            SnackbarUtils.with(ActivityUtils.getTopActivity().window.decorView)
                .setMessage("无法连接网络，请检查网络")
                .setDuration(SnackbarUtils.LENGTH_SHORT)
                .setAction(StringUtils.getString(R.string.setting)) {
                    ActivityUtils.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
                .setBottomMargin(AutoSizeUtils.dp2px(BaseApplication.getAppContext(),20f))
                .show()
        }
    }

    override fun onNetworkType(type: String) {

    }

    override fun onNetworkChange() {
    }
}