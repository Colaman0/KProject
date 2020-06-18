package com.kyle.colaman.activity

import androidx.databinding.ObservableField
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivitySearchBinding
import com.kyle.colman.view.KActivity

class SearchActivity : KActivity<ActivitySearchBinding>(R.layout.activity_search) {
    val queryText = ObservableField("").apply {
    }

    override fun initView() {
        binding?.activity = this
    }

    /**
     * 清除输入框内容
     *
     */
    fun clearQuery() {

    }
}