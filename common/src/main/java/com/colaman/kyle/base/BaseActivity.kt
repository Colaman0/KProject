package com.colaman.kyle.base

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.colaman.kyle.BR
import com.colaman.kyle.R
import com.colaman.kyle.base.viewmodel.BaseViewModel
import com.colaman.kyle.common.helper.DoubleClickExitInterceptor
import com.colaman.kyle.impl.IBackpressInterceptor
import com.colaman.kyle.impl.IStatus
import com.colaman.statuslayout.StatusLayout
import com.gyf.barlibrary.ImmersionBar


/**
 * Create by kyle on 2018/9/19
 * Function : baseActivity
 *
 * 1.通过重写[needStatusLayout]返回值来确定是否需要使用[StatusLayout]作为根布局，默认为true，控制[statusLayout]对象来控制页面状态
 *
 * 2.[binding]为页面的binding，但是不包括[statusLayout]  [viewModel]是绑定到页面的viewModel，具体逻辑都在viewModel内部
 *
 * 3.[mImmersionBar] 设置状态栏相关属性
 */
typealias activityResult = (requestCode: Int, resultCode: Int, data: Intent?) -> Unit

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(), IStatus {
    // 状态栏颜色
    private val mDefaultStatusBarColorRes = R.color.white
    var mImmersionBar: ImmersionBar? = null
    private val activityResults = mutableListOf<activityResult>()
    var statusLayout: StatusLayout? = null
    lateinit var binding: B
    var viewModel: VM? = null


    val context: Context
        get() = this

    val activity: Activity
        get() = this


    override fun onCreate(savedInstanceState: Bundle?) {
        // 初始化，绑定生命周期
        viewModel = createViewModel()
        viewModel?.bindLife(this)
        super.onCreate(savedInstanceState)
        val rootView: View?
        if (needStatusLayout()) {
            statusLayout = getStatusLayoutInstance(this, initLayoutRes())
            rootView = statusLayout
        } else {
            rootView = LayoutInflater.from(this).inflate(initLayoutRes(), null)
        }
        setContentView(rootView)
        initBinding(rootView!!)
        initStatusLayout()
        initStatusBar()
        initView()
    }


    /**-+
     * 初始化databinding
     * @param rootView View activity的根view
     */
    open fun initBinding(rootView: View) {
        binding = (if (rootView is StatusLayout) {
            DataBindingUtil.bind(rootView.getDefaultContentView())
        } else {
            DataBindingUtil.bind(rootView)
        })!!
        if (viewModel != null) {
            binding.setVariable(BR.viewmodel, viewModel)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        super.onDestroy()
        destroyStatusBar()
    }

    @LayoutRes
    protected abstract fun initLayoutRes(): Int

    protected abstract fun initView()

    abstract fun createViewModel(): VM?

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
    private fun destroyStatusBar() {
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

    fun goToActivity(activity: Class<*>) {
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
    protected open fun getStatusLayoutInstance(context: Context, @LayoutRes layoutRes: Int) =
            StatusLayout.init(context, layoutRes)

    protected open fun getStatusLayoutInstance(view: View) =
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


    val backpressInterceptors by lazy {
        mutableListOf<IBackpressInterceptor>(DoubleClickExitInterceptor())
    }

    override fun onBackPressed() {
        backpressInterceptors.forEach {
            if (it.OnInterceptor(this)) {
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
    fun addBackpressInterceptor(interceptor: IBackpressInterceptor, index: Int = 0) {
        backpressInterceptors.add(index, interceptor)
    }


    /**
     * 结束activity
     */
    fun back(view: View?) {
        finish()
    }

}
