package com.kyle.colaman.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colaman.R

/**
 * Author   : kyle
 * Date     : 2020/6/15
 * Function : pagingadapter
 */
class PagingAdapter(context: Context) :
    PagingDataAdapter<PagingItemView<Any>, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<PagingItemView<Any>>() {
            override fun areItemsTheSame(
                oldItem: PagingItemView<Any>,
                newItem: PagingItemView<Any>
            ): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(
                oldItem: PagingItemView<Any>,
                newItem: PagingItemView<Any>
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
            getItem(position)?.onBindView(holder = holder as PagingVHolder, position = position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = PagingVHolder(layoutInflater.inflate(viewType, parent, false))
        return holder
    }
}

class StateAdapter : LoadStateAdapter<LoadmoreView>() {
    override fun onBindViewHolder(holder: LoadmoreView, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadmoreView {
        return LoadmoreView(parent, loadState)
    }

}

class LoadmoreView(val parent: ViewGroup, val loadState: LoadState) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)!!.inflate(R.layout.item_loadmore, parent, false)
    ) {

    init {
        bindState(loadState)
    }

    fun bindState(loadState: LoadState) {

    }
}

class PagingVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}

abstract class PagingItemView<T : Any>(@LayoutRes val layoutRes: Int) {
//    val binding by lazy {
//        DataBindingUtil.bind<B>(holder.itemView)
//    }

    lateinit var holder: PagingVHolder

    open fun onBindView(holder: PagingVHolder, position: Int) {
        this.holder = holder
    }

    abstract fun areItemsTheSame(data: T): Boolean

    abstract fun areContentsTheSame(data: T): Boolean
}