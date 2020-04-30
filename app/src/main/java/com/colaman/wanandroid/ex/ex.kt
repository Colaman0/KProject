package com.colaman.wanandroid.ex

import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import com.colaman.wanandroid.R

/**
 * Author   : kyle
 * Date     : 2020/4/28
 * Function : 拓展方法
 */


fun EditText.bindPasswordVisible(view: ImageView, visible: Boolean = false) {
    var visibleFlag = visible
    inputType = if (visibleFlag) {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    setSelection(text.length)
    view.setImageResource(if (visibleFlag) R.mipmap.psw_visible else R.mipmap.psw_invisible)
    view.setOnClickListener {
        visibleFlag = !visibleFlag
        inputType = if (visibleFlag) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setImageResource(if (visibleFlag) R.mipmap.psw_visible else R.mipmap.psw_invisible)
        setSelection(text.length)
    }
}