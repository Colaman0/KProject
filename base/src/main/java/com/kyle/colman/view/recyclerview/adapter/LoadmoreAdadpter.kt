package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.R
import com.kyle.colman.recyclerview.PagingVHolder

/**
 * Author   : kyle
 * Date     : 2020/7/13
 * Function : loadmore adpater
 */
class LoadmoreAdadpter(
    @LayoutRes val layoutRes: Int,
    val context: Context
) : RecyclerView.Adapter<PagingVHolder>() {
    var rootView: View? = null
    var disableLoadmore = true
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagingVHolder {
        rootView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
        return PagingVHolder(itemView = rootView!!)
    }

    override fun getItemCount(): Int {
        return if (disableLoadmore) 1 else 0
    }

    override fun onBindViewHolder(holder: PagingVHolder, position: Int) {
    }

    fun disableLoadmore(disable: Boolean) {
        disableLoadmore = disable
        notifyItemChanged(0)
    }
}