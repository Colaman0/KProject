package com.colaman.kyle.view

import android.view.View
import com.colaman.kyle.R
import com.google.android.material.snackbar.Snackbar

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : snackbar配置
 *
 */
data class SnackBarConfig(
    var next: SnackBarConfig? = null,
    var before: SnackBarConfig? = null,
    var visible: Boolean = false,
    var snackbar: Snackbar? = null,
    var duration: Int? = 2,
    var config: SnackBarViewConfig? = null
)

data class SnackBarViewConfig(
    var msg: String? = "",
    var actionText: String? = "",
    var msgColor: Int? = R.color.black,
    var bgColor: Int? = R.color.white,
    var bgResource: Int? = R.drawable.bg_default_snackbar,
    var actionTextColor: Int? = R.color.black,
    var actionListener: View.OnClickListener? = null,
    var bottomMargin: Int = 0
)