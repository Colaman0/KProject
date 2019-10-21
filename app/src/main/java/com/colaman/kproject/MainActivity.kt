package com.colaman.kproject

import android.content.Entity
import com.colaman.kproject.databinding.ActivityMainBinding
import com.colaman.kproject.entity.PersonEntity
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.view.CommonWebView

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {
        binding.webView.load("https://bbs.hupu.com/bxj")
            .bindActivity(this)

        binding.webView.callJs(CommonWebView.JSBuilder()
            .method("testJs")
            .addParam(1)
            .addParam(2.345)
            .addParam("test")
            .addParam(PersonEntity())
        )
    }
}
