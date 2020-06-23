package com.kyle.colaman

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.kyle.colaman.activity.WebActivity
import com.kyle.colaman.helper.PocketRoom
import com.kyle.colman.config.Constants
import com.kyle.colman.view.KApplication
import com.kyle.colman.view.buildIntent

/**
 * Author   : kyle
 * Date     : 2020/6/12
 * Function : 拓展
 */

fun gotoWeb(activity: Activity, url: String, title: String, id: Int,desc:String) {
    val intent = buildIntent(activity, WebActivity::class.java)
    intent.putExtra(Constants.DATA, url)
    intent.putExtra(Constants.TITLE, title)
    intent.putExtra(Constants.ID, id)
    intent.putExtra(Constants.DESC, desc)
    activity.startActivity(intent)
}

/**
 * 复制内容到剪切板
 *
 * @param copyStr
 * @return
 */
fun copyToBorad(context: Context, text: String) {
    try {
        //获取剪贴板管理器
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val data = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(data);
    } catch (e: Exception) {

    }
}

fun getPocketRoom() = PocketRoom.getDatabase(KApplication.getAppContext()!!).pocketDao()