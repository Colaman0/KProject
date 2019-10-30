package com.colaman.kproject

import android.view.View
import com.colaman.kproject.api.Api
import com.colaman.kproject.databinding.ActivityRecyclerViewBinding
import com.colaman.kproject.viewmodel.ItemTextViewmodel
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.common.expand.bindLinearAdapter
import com.colaman.kyle.common.helper.PhotoPickerHelper
import com.colaman.kyle.common.recyclerview.adapter.FeaturesRecyclerViewAdapter
import com.colaman.kyle.common.rx.bindStatusImpl
import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.view.CommonDialog

class RecyclerViewActivity : BaseActivity<ActivityRecyclerViewBinding>() {
    val adapter by lazy {
        FeaturesRecyclerViewAdapter(this)
    }

    override fun initLayoutRes() = R.layout.activity_recycler_view

    override fun initView() {

        binding.recyclerview.bindLinearAdapter(this, adapter)
        Api.getTab()
            .bindStatusImpl(
                CommonDialog(this@RecyclerViewActivity).setBuilder(
                    CommonDialog.CommonDialogBuilder(
                        textColor = R.color.colorAccent, textSize = 24, progressSize = 40
                    )
                )
            )
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