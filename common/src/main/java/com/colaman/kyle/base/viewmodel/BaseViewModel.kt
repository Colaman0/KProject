package com.colaman.kyle.base.viewmodel

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.colaman.kyle.common.LamdaRunnable
import java.util.*

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/19
 *     desc   : 基于LifeViewModel封装逻辑，activity/fragment会用到，recyclerview用BindingViewModel
 *     用哪个viewmodel 还是看需不需要用到databinding，BindingViewModel带有databinding，直接操作view，不同于常规的viewmodel层
 * </pre>
 */
open class BaseViewModel : DefaultLifecycleObserver {
    var lifecycleOwner: LifecycleOwner? = null

    /**
     * 监听生命周期
     */
    fun bindLife(lifecycleOwner: LifecycleOwner?) {
        this@BaseViewModel.lifecycleOwner = lifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    /**
     * 绑定一个viewmodel
     */
    fun bindParentViewModel(viewModel: BaseViewModel?) {
        bindLife(viewModel?.lifecycleOwner)
    }

    var context: Context? = null

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


