package com.colaman.kyle.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *
 *     author : kyle
 *     time   : 2019/10/28
 *     desc   : activity生命周期监听
 *
 */

open class ActivityLifeCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
    }
}