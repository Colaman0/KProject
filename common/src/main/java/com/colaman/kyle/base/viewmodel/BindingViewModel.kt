package com.colaman.kyle.base.viewmodel

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR


/**
 * Create by kyle on 2019/7/18
 * Function : 带有databinding的viewmodel， 处理一些databinding相关的逻辑
 */
open class BindingViewModel<B : ViewDataBinding> : BaseViewModel() {
    open var binding: B? = null
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


}