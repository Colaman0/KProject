package com.kyle.colaman.base.viewmodel

import androidx.lifecycle.ViewModel

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/19
 *     desc   : 基于LifeViewModel封装逻辑，activity/fragment会用到，recyclerview用BindingViewModel
 *     用哪个viewmodel 还是看需不需要用到databinding，BindingViewModel带有databinding，直接操作view，不同于常规的viewmodel层
 * </pre>
 */
open class BaseViewModel : ViewModel() {

}


