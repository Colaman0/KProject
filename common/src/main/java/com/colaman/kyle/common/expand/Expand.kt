package com.colaman.kyle.common.expand

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