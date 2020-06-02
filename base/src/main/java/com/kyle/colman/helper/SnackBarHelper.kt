package com.kyle.colman.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.SnackbarUtils
import com.google.android.material.snackbar.Snackbar
import com.kyle.colman.view.SnackBarConfig

/**
 *
 *     author : kyle
 *     time   : 2019/10/26
 *     desc   : snackbar辅助类
 *
 */
@SuppressLint("StaticFieldLeak")
object SnackBarHelper : Application.ActivityLifecycleCallbacks {
    /**
     * 正在展示的snackbar
     */
    val visibleSnackbar by lazy {
        mutableListOf<Snackbar?>()
    }

    val waitSnackbar by lazy {
        mutableListOf<SnackBarConfig>()
    }

    var currentSnackbarConfig: SnackBarConfig? = null

    var currentActivity: Activity? = null

    init {
        TimeHelper.globalTimer
            .filter { currentSnackbarConfig != null && it > currentSnackbarConfig!!.endTime!! }
            .doOnNext {
                reset()
            }
            .subscribe()
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {

    }

    private fun showSnackbar(activity: Activity?) {
        if (activity != null &&
            currentSnackbarConfig != null &&
            !currentSnackbarConfig!!.activityExist(activity)
        ) {
            visibleSnackbar.add(configToSnackbar(currentSnackbarConfig!!, activity))
        }
    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
        // 显示新的snackbar
        showSnackbar(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    fun push(config: SnackBarConfig) {
        reset()
        currentSnackbarConfig = config
        showSnackbar(currentActivity)
    }

    /**
     * 重置
     */
    private fun reset() {
        currentSnackbarConfig?.activities?.clear()
        currentSnackbarConfig = null
        // dismiss所有snackbar
        visibleSnackbar.forEach { it?.dismiss() }
        visibleSnackbar.clear()
    }


    fun configToSnackbar(config: SnackBarConfig, activity: Activity): Snackbar {
        config.addActivity(activity)
        val snackbar = SnackbarUtils.with(activity.window.decorView)
            .run {
                if (!config.actionText.isNullOrBlank()) {
                    setAction(config.actionText!!, config.actionListener!!)
                }
                setBgColor(config.bgColor!!)
                setBgResource(config.bgResource!!)
                setMessage(config.msg!!)
                setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                setMessageColor(config.msgColor!!)
                show()
            }

        return snackbar
    }
}

