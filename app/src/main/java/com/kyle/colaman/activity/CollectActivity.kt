package com.kyle.colaman.activity

import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.kyle.colaman.R
import com.kyle.colaman.entity.CollectEntity
import com.kyle.colaman.source.CollectPageSource
import com.kyle.colaman.source.CollectSource
import com.kyle.colaman.viewmodel.ItemCollectViewmodel
import com.kyle.colman.config.PageViewConfig
import com.kyle.colman.recyclerview.PagingFragment
import com.kyle.colman.recyclerview.RecyclerViewFragment
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_collect.*


@OptIn(ExperimentalPagingApi::class)
class CollectActivity : KActivity<Nothing>(R.layout.activity_collect) {
    val recyclerViewFragment by lazy {
        recyclerview_framgent as RecyclerViewFragment<CollectEntity>
    }

    override fun initView() {
        initToolbar()
        recyclerViewFragment.initPageSource(CollectPageSource(lifecycleScope,this))
//        recyclerViewFragment.initPagerConfig(PageViewConfig<CollectEntity>({
//            CollectSource()
//        }, uiTrans = { entity, adapter ->
//            ItemCollectViewmodel(entity, lifecycleOwner = this)
//        }))
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }


}