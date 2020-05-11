package com.colaman.kyle.base.viewmodel

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.colaman.kyle.common.LamdaRunnable
import java.util.*


/**
 * Create by kyle on 2019/7/18
 * Function : 带有databinding的viewmodel， 处理一些databinding相关的逻辑
 */
open class BindingViewModel<B : ViewDataBinding> : DefaultLifecycleObserver {
    open var binding: B? = null

    var context: Context? = null

    /**
     * 设置一个默认的variableId,用于xml中绑定当前viewmodel
     */
    private var variableId = getDefaultVariableId()


    open fun setViewDatabinding(binding: ViewDataBinding?) {
        try {
            this.binding = binding as B?
            refreshBindingData(variableId)
        } catch (e: Exception) {
        }

    }

    fun getViewDatabing() = binding

    /**
     * 刷新databinding的数据
     * @param id 对应BR类的id
     * @param data BR类id对应的数据
     */
    open fun refreshBindingData(id: Int = getDefaultVariableId(), data: Any = this) {
        binding?.setVariable(id, data)
        binding?.executePendingBindings()
    }


    /**
     * xml中如果不是用viewmodel作为name来绑定viewmodel，则需要重写这个方法，或者手动调用setVariable来设置数据
     */
    fun getDefaultVariableId() = BR.viewmodel

    var lifecycleOwner: LifecycleOwner? = null

    /**
     * 监听生命周期
     */
    fun bindLife(lifecycleOwner: LifecycleOwner?) {
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /**
     * 绑定一个viewmodel
     */
    fun bindParentViewModel(viewModel: BindingViewModel<*>) {
        bindLife(viewModel?.lifecycleOwner)
    }


    /**
     * 存放一些等view attach之后执行的lamda函数，做一个延迟执行的处理
     */
    private val mActiveRunnable = Collections.synchronizedList(mutableListOf<LamdaRunnable>())

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        /**
         *  遍历任务list执行其中的lamda函数
         */
        while (mActiveRunnable.isNotEmpty()) {
            mActiveRunnable.removeAt(0).start()
        }
    }


    /**
     * 缓存一些等到生命周期[onStart] 之后执行的操作
     * @param runnable LamdaRunnable 里面存放一段Lamda
     */
    fun pushOnStartRunnable(runnable: LamdaRunnable) {
        mActiveRunnable.add(runnable)
    }


}