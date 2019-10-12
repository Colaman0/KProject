package com.colaman.common.base

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.colaman.common.R
import com.colaman.common.impl.IStatus
import com.colaman.statuslayout.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import me.yokeyword.fragmentation.SupportActivity


/**
 * Create by kyle on 2018/9/19
 * Function : baseActivity
 *
 * 1.通过重写[needStatusLayout]返回值来确定是否需要使用[StatusLayout]作为根布局，默认为true，控制[statusLayout]对象来控制页面状态
 *
 * 2.[binding]为页面的binding，但是不包括[statusLayout]  [viewModel]是绑定到页面的viewmodel，具体逻辑都在viewmodel内部
 *
 * 3.[mImmersionBar] 设置状态栏相关属性
 */
typealias activityResult = (requestCode: Int, resultCode: Int, data: Intent?) -> Unit

abstract class BaseActivity<B : ViewDataBinding> : SupportActivity(), IStatus {
    // 状态栏颜色
    private val mDefaultStatusBarColorRes = R.color.white
    var mImmersionBar: ImmersionBar? = null
    private val activityResults = mutableListOf<activityResult>()
    var statusLayout: StatusLayout? = null
    protected var binding: B? = null


    val context: Context
        get() = this

    val activity: Activity
        get() = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView: View?
        if (needStatusLayout()) {
            statusLayout = getStatusLayoutInstance(this, initLayoutRes())
            rootView = statusLayout
        } else {
            rootView = LayoutInflater.from(this).inflate(initLayoutRes(), null)
        }
        setContentView(rootView)
        initBindind(rootView!!)
        initStatusLayout()
        initStatusBar()
        initView()

    }


    /**
     * 初始化databinding
     * @param rootView View activity的根view
     */
    fun initBindind(rootView: View) {
        binding = if (rootView is StatusLayout) {
            DataBindingUtil.bind(rootView.getDefaultContentView())
        } else {
            DataBindingUtil.bind(rootView)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
        destoryStatusBar()
    }

    @LayoutRes
    protected abstract fun initLayoutRes(): Int

    protected abstract fun initView()


    /**
     * 设置状态栏颜色
     */
    private fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar!!
            .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            .keyboardEnable(true)
            .statusBarDarkFont(true, 0.2f)
            .statusBarColor(setStatusBarColor())
            .init()
    }

    /**
     * 加载多布局管理
     */
    private fun initStatusLayout() {

    }

    /**
     * 释放关于状态栏的资源
     */
    private fun destoryStatusBar() {
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @return
     */
    @ColorRes
    protected fun setStatusBarColor(): Int {
        return mDefaultStatusBarColorRes
    }

    fun getDefaultIntent(activity: Class<*>): Intent {
        return Intent(this, activity)
    }

    fun goToAcitivty(activity: Class<*>) {
        startActivity(getDefaultIntent(activity))
    }

    fun switchLayout(layoutType: String) {
        statusLayout?.switchLayout(layoutType)
    }

    @Synchronized
    fun startForegroundServiceAsUser(service: Intent, user: UserHandle): ComponentName? {
        return null
    }

    /**
     * 是否需要添加statuslayout来处理布局状态
     *
     * @return
     */
    fun needStatusLayout(): Boolean {
        return true
    }


    override fun success() {
        statusLayout?.showDefaultContent()
    }

    override fun failed() {
        statusLayout?.switchLayout(StatusLayout.STATUS_ERROR)
    }

    override fun start() {
        statusLayout?.switchLayout(StatusLayout.STATUS_LOADING)
    }

    override fun destroy() {

    }

    /**
     * 这两个方法是为了拓展出更多不同statuslayout的实现而声明的，比如特定情况下拓展出CustomStatusLayout用于绑定rxjava流
     * 就可以重写其中一个方法来给出具体实现
     */
    open protected fun getStatusLayoutInstance(context: Context, @LayoutRes layoutRes: Int) =
        StatusLayout.init(context, layoutRes)

    open protected fun getStatusLayoutInstance(view: View) =
        StatusLayout.init(view)

    /**
     * 添加[onActivityResult]的回调监听，
     * @param callback Function3<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Intent?, Unit>
     */
    fun registerActivityResult(callback: activityResult) {
        activityResults.add(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        RESULT_OK

        super.onActivityResult(requestCode, resultCode, data)
        /**
         * 把结果都分发出去
         */
        activityResults.forEach {
            it.invoke(requestCode, resultCode, data)
        }
    }

}
