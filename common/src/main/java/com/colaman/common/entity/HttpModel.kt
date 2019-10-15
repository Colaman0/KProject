package com.colaman.common.entity

import okhttp3.Request

/**
 *
 *     author : kyle
 *     time   : 2019/10/15
 *     desc   : http信息
 *
 */

data class HttpModel(
    var request: Request? = null
    )