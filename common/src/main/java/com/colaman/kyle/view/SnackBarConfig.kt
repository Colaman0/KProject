package com.colaman.kyle.view

import android.app.Activity
import android.util.TimeUtils
import android.view.View
import com.blankj.utilcode.util.SnackbarUtils
import com.colaman.kyle.R
import java.lang.ref.WeakReference

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : snackbar配置
 *
 */
data class SnackBarConfig(
    var duration: Int? = SnackbarUtils.LENGTH_SHORT,
    var endTime: Long? = System.currentTimeMillis() + (duration!! * 1000),
    var msg: String? = "",
    var actionText: String? = "",
    var msgColor: Int? = R.color.black,
    var bgColor: Int? = R.color.white,
    var bgResource: Int? = R.drawable.bg_default_snackbar,
    var actionTextColor: Int? = R.color.black,
    var actionListener: View.OnClickListener? = View.OnClickListener { },
    var bottomMargin: Int = 0
) {

    val activities = mutableListOf<WeakReference<Activity>>()

    fun addActivity(activity: Activity) {
        activities.add(WeakReference(activity))
    }

    fun removeActivity(activity: Activity) {
        activities.forEach {
            if (it.get() == activity) {
                activities.remove(it)
            }
        }
    }

    fun activityExist(activity: Activity): Boolean {
        activities.forEach {
            if (it.get() == activity) {
                return true
            }
        }
        return false
    }
}
