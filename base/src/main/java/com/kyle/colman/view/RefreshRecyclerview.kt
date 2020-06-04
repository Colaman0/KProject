package com.kyle.colman.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyle.colaman.common.recyclerview.layoutmanager.WrapLinearlayoutManager
import com.kyle.colman.R
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.view.recyclerview.adapter.KAdapter
import kotlinx.android.synthetic.main.view_refresh_recyclerview.view.*

/**
 * Author   : kyle
 * Date     : 2020/6/4
 * Function : 下拉刷新recyclerview
 */
class RefreshRecyclerview : ConstraintLayout {
    var kAdapter: KAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.view_refresh_recyclerview, this);

        recyclerview_k.apply {
            kAdapter = KAdapter(context)
            init(
                kAdapter,
                WrapLinearlayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
                    recycleChildrenOnDetach = true
                }, context as LifecycleOwner
            )
        }
        recyclerview_k.setRefreshView(refresh_layout_k)
    }

    fun setDataCreator(datacreator: IRVDataCreator<*>) {
        recyclerview_k.dataCreator = (datacreator as IRVDataCreator<Any>)
    }
}