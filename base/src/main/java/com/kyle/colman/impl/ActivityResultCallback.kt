package com.kyle.colman.impl

import android.content.Intent
import android.os.Bundle

/**
 * Author   : kyle
 * Date     : 2020/6/7
 * Function : activity result  回调
 */

interface ActivityResultCallback {
    fun onResult(resultCode: Int, data: Intent?)
}