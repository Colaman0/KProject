package com.kyle.colman.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.kyle.colman.view.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import com.kyle.colman.R
import com.kyle.colman.impl.ActivityResultCallback
import kotlin.system.exitProcess

/**
 * Author   : kyle
 * Date     : 2020/5/29
 * Function : base activity
 */
abstract class KActivity<B : ViewDataBinding>(
    @LayoutRes
    val contentLayoutId: Int,
    val needStatusLayout: Boolean = false,
    @ColorRes val statusbarColor: Int = R.color.white
) : AppCompatActivity() {
    var mImmersionBar: ImmersionBar? = null
    var statusLayout: StatusLayout? = null
    var binding: B? = null

    /**
     * 用于调用[startActivityForResult]的自增requestcode
     */
    var startActivityRequestCode = 0
    var activityResultCallback: ActivityResultCallback? = null

    val backpressInterceptors by lazy {
        mutableListOf(doubleClickExitBlock())
    }

    val context: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTaskRoot) { // 当前类不是该Task的根部，那么之前启动
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) { // 当前类是从桌面启动的
                    finish() // finish掉该类，直接打开该Task中现存的Activity
                    return
                }
            }
        }
        if (needStatusLayout) {
            val view = layoutInflater.inflate(contentLayoutId, window.decorView as ViewGroup, false)
            statusLayout = StatusLayout.init(view)
            setContentView(statusLayout)
            binding = DataBindingUtil.findBinding(view)
        } else {
            binding = DataBindingUtil.setContentView(this, contentLayoutId)
        }
        binding?.lifecycleOwner = this
        initStatusBar()
        initView()
    }

    abstract fun initView()

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
        startActivity(buildIntent(this, activity))
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionBar?.destroy()
    }

    // TODO: 2020/6/7 activity result的封装 关注ActivityResultContracts
}

fun buildIntent(context: Context, activity: Class<*>): Intent {
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
            }
            true
        } else {
            false
        }
    }
}
