package com.kyle.colman.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.colaman.statuslayout.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import com.kyle.colman.R
import kotlin.system.exitProcess

/**
 * Author   : kyle
 * Date     : 2020/5/29
 * Function : base activity
 */
open class KActivity<B : ViewDataBinding>(
    val contentLayoutId: Int,
    val needStatusLayout: Boolean = false,
    @ColorRes val statusbarColor: Int = R.color.white
) : AppCompatActivity() {
    var mImmersionBar: ImmersionBar? = null
    var statusLayout: StatusLayout? = null
    lateinit var binding: B

    val backpressInterceptors by lazy {
        mutableListOf(doubleClickExitBlock())
    }

    val context: Context
        get() = this


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (needStatusLayout) {
            statusLayout = StatusLayout.init(context, contentLayoutId)
            setContentView(statusLayout)
            binding = DataBindingUtil.findBinding<B>(statusLayout!!)!!
        } else {
            binding = DataBindingUtil.setContentView(this, contentLayoutId)
        }
        initStatusBar()
    }


    /**
     * 设置状态栏颜色
     */
    private fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar!!
            .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            .keyboardEnable(true)
            .statusBarDarkFont(true, 0.2f)
            .statusBarColor(statusbarColor)
            .init()
    }


    override fun onBackPressed() {
        backpressInterceptors.forEach {
            if (it(this)) {
                return
            }
        }
        super.onBackPressed()
    }

    /**
     * 添加返回按钮的拦截器
     * @param interceptor IBackpressInterceptor
     * @param index Int
     */
    fun addBackpressInterceptor(interceptor: (KActivity<*>) -> Boolean, index: Int = 0) {
        backpressInterceptors.add(index, interceptor)
    }

    fun goToActivity(activity: Class<*>) {
        startActivity(getDefaultIntent(this, activity))
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionBar?.barColor(statusbarColor)
        mImmersionBar?.destroy()
    }
}

fun getDefaultIntent(context: Context, activity: Class<*>): Intent {
    return Intent(context, activity)
}

fun doubleClickExitBlock(): ((KActivity<*>) -> Boolean) {
    var lastClick = 0L

    return { activity ->
        if (ActivityUtils.getActivityList().size <= 1) {
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp - lastClick < 300) {
                activity.finish()
                exitProcess(0)
            } else {
                ToastUtils.showShort("再按一次退出应用")
                lastClick = currentTimestamp
                true
            }
        }
        false
    }
}
