package com.colaman.common.common.recyclerview.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.PrimitiveKind
import java.lang.Exception

/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : 适配GridLayoutManager
 *
 */
class WrapGridlayoutManager(
    context: Context,
    spacount: Int,
    orientation: Int,
    reverse: Boolean
) : GridLayoutManager(context, spacount, orientation, reverse) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {

        }
    }
}