package com.colaman.kyle.imp

import android.content.Context

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/27
 *     desc   :
 * </pre>
 */
interface IRxObserverLife {
    fun onStart(context: Context)

    fun onEnd(context: Context)

}