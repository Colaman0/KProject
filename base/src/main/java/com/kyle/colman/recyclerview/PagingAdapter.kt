package com.kyle.colman.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils

/**
 * Author   : kyle
 * Date     : 2020/6/15
 * Function : pagingadapter
 */
class PagingAdapter(context: Context) :
    PagingDataAdapter<PagingItemView<Any, *>, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<PagingItemView<Any, *>>() {
            override fun areItemsTheSame(
                oldItem: PagingItemView<Any, *>,
                newItem: PagingItemView<Any, *>
            ): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(
                oldItem: PagingItemView<Any, *>,
                newItem: PagingItemView<Any, *>
            ): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }
        }
    ) {

    val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            getItem(position)?.bindView(holder = holder as PagingVHolder, position = position)
        }
    }

    suspend fun submitItem(pagingData: PagingData<in PagingItemView<Any, *>>) {
        super.submitData(pagingData as PagingData<PagingItemView<Any, *>>)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PagingVHolder(layoutInflater.inflate(viewType, parent, false))
    }
}


