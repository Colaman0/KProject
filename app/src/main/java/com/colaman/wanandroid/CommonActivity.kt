package com.colaman.wanandroid

import android.view.View
import androidx.databinding.ViewDataBinding
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.base.viewmodel.BaseViewModel

/**
 * Author   : kyle
 * Date     : 2020/4/27
 * Function : common
 */
abstract class CommonActivity<B : ViewDataBinding, VM : BaseViewModel> : BaseActivity<B, VM>() {


    override fun initBinding(rootView: View) {
        super.initBinding(rootView)
        binding.setVariable(BR.activity, this)
    }
}