package com.kyle.colaman

import android.app.Activity
import com.kyle.colaman.activity.WebActivity
import com.kyle.colman.config.Constants
import com.kyle.colman.view.buildIntent

/**
 * Author   : kyle
 * Date     : 2020/6/12
 * Function : 拓展
 */

fun gotoWeb(activity: Activity, url: String, title: String) {
    val intent = buildIntent(activity, WebActivity::class.java)
    intent.putExtra(Constants.DATA, url)
    intent.putExtra(Constants.TITLE, title)
    activity.startActivity(intent)
}