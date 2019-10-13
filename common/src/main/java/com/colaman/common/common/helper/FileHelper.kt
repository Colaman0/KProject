package com.colaman.common.common.helper

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/6
 *     desc   :
 * </pre>
 */
class FileHelper {

    companion object {
        fun getCacheFile(context: Context) = ContextCompat.getExternalCacheDirs(context)
    }
}