package com.colaman.kproject

import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.colaman.common.base.BaseActivity
import com.colaman.common.base.recyclerview.adapter.FeaturesRecyclerViewAdapter
import com.colaman.common.common.expand.bindLinearAdapter
import com.colaman.common.common.rx.bindStatusImpl
import com.colaman.common.common.rx.fullSubscribe
import com.colaman.common.impl.IStatus
import com.colaman.common.view.CommonDialog
import com.colaman.kproject.api.Api
import com.colaman.kproject.databinding.ActivityRecyclerViewBinding
import com.colaman.kproject.viewmodel.ItemTextViewmodel

class RecyclerViewActivity : BaseActivity<ActivityRecyclerViewBinding>() {
    val adapter by lazy {
        FeaturesRecyclerViewAdapter(this)
    }

    override fun initLayoutRes() = R.layout.activity_recycler_view

    override fun initView() {

        binding.recyclerview.bindLinearAdapter(this, adapter)
        Api.getTab()
            .doOnNext {
                LogUtils.d(it)
            }
            .bindStatusImpl(CommonDialog(this@RecyclerViewActivity))
            .fullSubscribe()
    }


    fun add(view: View?) {
        adapter.add(ItemTextViewmodel(adapter.size().toString()))
        adapter.switchLoadMore(true, false)
        adapter.diffNotifydatasetchanged()
    }

    fun remove(view: View) {
        adapter.remove(0)
        adapter.switchLoadMore(false, false)
        adapter.diffNotifydatasetchanged()
    }


}
