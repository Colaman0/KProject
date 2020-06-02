package com.kyle.colman.helper

import android.content.Context
import me.jessyan.autosize.utils.AutoSizeUtils

/**
 *
 *     author : kyle
 *     time   : 2019/10/17
 *     desc   : 拓展方法
 *
 */

fun dp2px(context: Context, value: Int) = if (value > 0) {
    AutoSizeUtils.dp2px(context, value.toFloat())
} else {
    value
}

val Any.tags by lazy {
    return@lazy HashMap<String, Any>()
}

inline fun <reified T> Any.getTag(key: String): T? {
    val data = tags[key]
    if (data is T) {
        return data
    }
    return null
}

fun Any.putTag(key: String, value: Any) {
    tags[key] = value
}

/**
 * 作为布尔值判断的lamda简写
 */
fun Boolean?.yes(callback: () -> Unit) {
    if (this == true) {
        callback()
    }
}


fun Boolean?.no(callback: () -> Unit) {
    if (this == false) {
        callback()
    }
}

