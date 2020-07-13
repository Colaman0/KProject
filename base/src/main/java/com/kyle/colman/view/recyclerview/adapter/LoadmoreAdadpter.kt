package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.R
import com.kyle.colman.databinding.LayoutLoadmoreBinding
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder

/**
 * Author   : kyle
 * Date     : 2020/7/13
 * Function : loadmore adpater
 */
class LoadmoreAdadpter(val context:Context) : RecyclerView.Adapter<PagingVHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagingVHolder{
        return PagingVHolder(LayoutInflater.from(context).inflate(R.layout.layout_loadmore,parent,false))
    }

    override fun getItemCount(): Int {return 1}
    override fun onBindViewHolder(holder: PagingVHolder, position: Int) {

    }
}