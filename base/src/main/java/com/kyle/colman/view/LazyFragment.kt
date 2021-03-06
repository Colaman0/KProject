package com.kyle.colman.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.leanback.app.BaseFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope

/**
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 懒加载fragment, 需要实现
 */
abstract class LazyFragment<B : ViewDataBinding>(layoutRes: Int) : KFragment<B>(layoutRes) {

    private var isCreateView = false
    private var isUserVisibility = false
    private var isFirstLoadFlag = true

    init {
        lifecycleScope.launchWhenResumed {
            lazyLoad()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dispatchLoadEvent()
    }

    /**
     *  {@link #onCreateView}
     * @param isVisibleToUser Boolean
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUserVisibility = isVisibleToUser
        dispatchLoadEvent()
    }


    /**
     *
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @param savedInstanceState Bundle?
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (view != null) {
            isCreateView = true
        }
        return view
    }

    /**
     * 分发处理事件
     */
    private fun dispatchLoadEvent() {
        if (isUserVisibility && isCreateView && isFirstLoadFlag) {
            isFirstLoadFlag = false
//            lazyLoad()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /**
         * 重置flag，view被销毁之后应该重新调用lazyLoad方法
         */
        isFirstLoadFlag = true
    }


    abstract fun lazyLoad()
}