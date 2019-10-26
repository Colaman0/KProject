package com.colaman.kyle.common.helper

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.SnackbarUtils
import com.colaman.kyle.view.SnackBarConfig
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : snackbar辅助类
 *
 */
object SnackBarHelper {
    @SuppressLint("StaticFieldLeak")
    private var head: SnackBarConfig? = null

    val barStatusLivedata by lazy {
        MutableLiveData<SnackBarConfig>()
    }

    fun push(config: SnackBarConfig, now: Boolean = false) {
        if (now) {
            head = config
            head?.snackbar?.show()
        } else {
            if (head != null) {
                head!!.next = config
            } else {
                head = config
                config.snackbar?.dismiss()
            }
        }
        barStatusLivedata.postValue(head?.apply {
            visible = true
        })
    }

}