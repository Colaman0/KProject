package com.colaman.kyle.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.colaman.kyle.base.fragment.BaseFragment

/**
 *     author : kyle
 *     time   : 2019/6/26
 *     desc   : 懒加载fragment, 需要实现
 */
abstract class LazyFragment< B : ViewDataBinding> : BaseFragment<B>() {

    var isCreateView = false
    var isUserVisibility = false
    var isFirstLoadFlag = true

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (view != null) {
            isCreateView = true
        }
        return view
    }

    /**
     * 分发处理事件
     */
    fun dispatchLoadEvent() {
        if (isUserVisibility && isCreateView && isFirstLoadFlag) {
            isFirstLoadFlag = false
            lazyLoad()
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