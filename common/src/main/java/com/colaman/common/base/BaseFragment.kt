package com.colaman.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.colaman.common.base.BaseActivity
import com.colaman.statuslayout.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import me.yokeyword.fragmentation.SupportFragment

/**
 *
 * <pre>
 *     author : kyle
 *     time   : 2019/6/26
 * 1.通过重写[needStatusLayout]返回值来确定是否需要使用[StatusLayout]作为根布局，默认为true，控制[statusLayout]对象来控制页面状态
 *
 * 2.[binding]为页面的binding，但是不包括[statusLayout] [viewModel]是绑定到页面的viewmodel，具体逻辑都在viewmodel内部
 *
 * 3.[mImmersionBar] 设置状态栏相关属性
 * </pre>
 */
abstract class BaseFragment<B : ViewDataBinding> : SupportFragment() {

    var statusLayout: StatusLayout? = null
    protected var binding: B? = null

    abstract fun initLayoutRes(): Int


    /**
     * 状态栏操作对象
     */
    private var mImmersionBar: ImmersionBar? = null
        get() {
            if (field === null) {
                if (activity is BaseActivity<*>) field =
                    (activity as BaseActivity<*>).mImmersionBar
            }
            return field
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * 设置statuslayout
         */
        val rootView =
            if (needStatusLayout()) {
                statusLayout = StatusLayout.init(context!!, initLayoutRes())
                statusLayout
            } else {
                LayoutInflater.from(context!!).inflate(initLayoutRes(), container, false)
            }
        initBindind(rootView!!)
        return rootView
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

    /**
     * 是否需要添加statuslayout来处理布局状态
     *
     * @return
     */
    open fun needStatusLayout(): Boolean {
        return true
    }


}