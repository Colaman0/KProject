package com.kyle.colaman.activity

import androidx.paging.ExperimentalPagingApi
import com.kyle.colaman.R
import com.kyle.colaman.entity.Constants
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_list.*

@OptIn(ExperimentalPagingApi::class)
class ListActivity : KActivity<Nothing>(R.layout.activity_list) {
    override fun initView() {

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(Constants.TITLE)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }
}