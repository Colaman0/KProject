package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.R
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.recyclerview.PagingVHolder
import com.kyle.colman.view.recyclerview.CommonDiffCallBack
import com.tencent.smtt.utils.p

/**
 * Author   : kyle
 * Date     : 2020/7/13
 * Function : 通用adapter
 */
open class CommonAdapter(val context: Context) : RecyclerView.Adapter<PagingVHolder>() {
    val items = mutableListOf<PagingItemView<*, *>>()
    val oldItems = mutableListOf<PagingItemView<*, *>>()
    val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    var loadmoreAdapter: LoadmoreAdadpter? = null
    val loadmoreLamda = {
        onLoadmore()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingVHolder {
        return PagingVHolder(layoutInflater.inflate(viewType, parent, false))
    }

    private fun getItem(position: Int): PagingItemView<*, *> {
        return oldItems[position]
    }

    override fun getItemCount(): Int {
        return oldItems.size
    }

    override fun onBindViewHolder(holder: PagingVHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            getItem(position).bindView(holder = holder, position = position)
        }
    }

    fun getEditableItems() = items

    fun diffNotify() {
        val result = DiffUtil.calculateDiff(CommonDiffCallBack(oldItems, items), true)
        result.dispatchUpdatesTo(this)
        oldItems.clear()
        oldItems.addAll(items)
    }

    fun bindRecyclerView(
        recyclerView: RecyclerView,
        loadmoreAdapter: LoadmoreAdadpter = LoadmoreAdadpter(
            R.layout.layout_loadmore,
            context
        ),
        headerAdapter: RecyclerView.Adapter<*>? = null
    ) {
        this.loadmoreAdapter = loadmoreAdapter
        val adapter = ConcatAdapter()
        headerAdapter?.apply { adapter.addAdapter(this) }
        adapter.addAdapter(this)
        adapter.addAdapter(loadmoreAdapter)
        recyclerView.adapter = adapter
        addLoadmoreCallback(loadmoreLamda)
    }

    fun disableLoadmore(disable: Boolean) {
        loadmoreAdapter?.disableLoadmore(disable)
    }

    fun addLoadmoreCallback(callback: () -> Unit) {
        loadmoreAdapter?.addLoadmoreCallback(callback)
    }

    open fun onLoadmore() {

    }
}