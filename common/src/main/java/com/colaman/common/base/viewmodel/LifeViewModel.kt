package com.colaman.common.base.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/9
 *     desc   : 带有生命周期相关处理的viewmodel基类
 * </pre>
 */
open class LifeViewModel : DefaultLifecycleObserver {
    var lifecycleOwner: LifecycleOwner? = null

    /**
     * 监听生命周期
     */
    fun bindLife(lifecycleOwner: LifecycleOwner?) {
        this@LifeViewModel.lifecycleOwner = lifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /**
     * 绑定一个viewmodel
     */
    fun bindParentViewModel(viewModel: LifeViewModel?) {
        bindLife(viewModel?.lifecycleOwner)
    }

}