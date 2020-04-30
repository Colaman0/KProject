package com.colaman.wanandroid

import com.colaman.kyle.base.BaseActivity
import com.colaman.wanandroid.databinding.ActivityMainBinding
import com.colaman.wanandroid.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun createViewModel() = MainViewModel()

    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {
    }
}
