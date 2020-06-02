package com.kyle.colaman.base.viewmodel

import androidx.databinding.ViewDataBinding
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colaman.view.LoadmoreCallback

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/1
 *     desc   : loadmoreitem的基类，封装一些方法
 * </pre>
 */
abstract class BaseLoadmoreView<B : ViewDataBinding, VM : Any>(layoutRes: Int) : RecyclerItemView<B, VM>(layoutRes)
        , LoadmoreCallback {

}