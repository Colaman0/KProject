package com.kyle.colman.helper

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : 时间辅助类
 *
 */
object TimeHelper {
    /**
     * 全局使用的一个时间发射器，100毫秒发射一次
     */
    val globalTimer by lazy {
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .map { System.currentTimeMillis() }
            .share()
    }

}