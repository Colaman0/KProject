package com.colaman.common.entity

import com.colaman.common.BuildConfig

/**
 * Create by kyle on 2018/9/26
 * Function :
 */
object Constants {
    object PATH {
        const val FILE_PROVIDER_PATH = BuildConfig.APPLICATION_ID + ".fileprovider"
    }



    object View {
        /**
         * 点击事件默认间隔
         */
        public const val SINGLE_CLICK_TIME = 500
    }
}
