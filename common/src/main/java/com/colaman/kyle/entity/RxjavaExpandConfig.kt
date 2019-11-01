package com.colaman.kyle.entity

import com.colaman.kyle.common.param.KError
import com.colaman.kyle.common.rx.KErrorLamdaRunnable
import com.colaman.kyle.impl.IExceptionAdapter
import com.colaman.kyle.impl.IRxConsumer
import com.colaman.kyle.impl.IStatus
import okhttp3.Request

/**
 *
 *     author : kyle
 *     time   : 2019/11/1
 *     desc   : rxjava拓展属性
 *
 */
data class RxjavaExpandConfig(
    val statusImpls: MutableList<IStatus> = mutableListOf(),
    val rxLiveDatas: MutableList<IStatus> = mutableListOf(),
    val kErrorAdapters: MutableList<IExceptionAdapter<KError>> = mutableListOf(),
    val rxConsumers: MutableList<IRxConsumer<in Any>> = mutableListOf(),
    var httpRequest: Request? = null,
    val kErrorCallback: KErrorLamdaRunnable = KErrorLamdaRunnable()
)