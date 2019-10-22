package com.colaman.kyle.common.recyclerview.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : 适配LinearLayoutManager
 *
 */
class WrapLinearlayoutManager(
    context: Context,
    orientation: Int,
    reverse: Boolean
) : LinearLayoutManager(context, orientation, reverse) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {

        }
    }
}