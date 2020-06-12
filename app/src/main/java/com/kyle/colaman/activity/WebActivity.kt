package com.kyle.colaman.activity

import com.kyle.colaman.R
import com.kyle.colman.config.Constants
import com.kyle.colman.databinding.LayoutRefreshWebviewBinding
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.bindUrl
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : KActivity<Nothing>(R.layout.activity_web) {
    private val url by lazy {
        intent.getStringExtra(Constants.DATA)
    }

    private val title by lazy {
        intent.getStringExtra(Constants.TITLE)
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar?.title = title
        web_view.apply {
            bindActivity(this@WebActivity)
        }
        web_view.load(this@WebActivity.url)
    }
}