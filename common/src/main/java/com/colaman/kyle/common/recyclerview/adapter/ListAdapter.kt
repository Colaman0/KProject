package com.colaman.kyle.base.recyclerview.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.colaman.kyle.base.recyclerview.RecyclerItemViewModel

/**
 * Create by kyle on 2019/1/9
 * Function : 操作数据源的adapter
 */
abstract class ListAdapter<VH : BaseViewHolder> : RecyclerView.Adapter<VH> {
    protected open var viewmodels = mutableListOf<RecyclerItemViewModel<out ViewDataBinding, out Any>?>()
    open var recyclerView: RecyclerView? = null
    open var context: Context? = null

    constructor(
            context: Context?,
            datas: MutableList<RecyclerItemViewModel<*, *>?> = mutableListOf()
    ) {
        this@ListAdapter.context = context
        viewmodels = datas
    }

    open fun getDatas() = viewmodels


    open fun size(): Int {
        return viewmodels.size
    }

    operator open fun get(index: Int): RecyclerItemViewModel<*, *>? {
        return viewmodels[index]
    }


    open fun add(viewModel: RecyclerItemViewModel<out ViewDataBinding, out Any>, index: Int = -1) {
        if (index == -1) {
            viewmodels.add(viewModel)
        } else {
            viewmodels.add(index, viewModel)
        }
        onDataChange(size())
    }

    open fun add(viewModel: RecyclerItemViewModel<out ViewDataBinding, out Any>) {
        viewmodels.add(viewModel)
        onDataChange(size())
    }

    open fun addAll(list: Collection<RecyclerItemViewModel<out ViewDataBinding, out Any>>) {
        viewmodels.addAll(list)
        onDataChange(size())
    }

    open fun addAll(list: Collection<RecyclerItemViewModel<out ViewDataBinding, out Any>>, index: Int = 0) {
        if (index < 0 || index >= size()) {
            return
        }
        if (index == -1) {
            viewmodels.addAll(list)
        } else {
            viewmodels.addAll(index, list)
        }
        onDataChange(size())
    }

    open fun remove(viewModel: RecyclerItemViewModel<out ViewDataBinding, out Any>) {
        if (canHandleData()) {
            viewmodels.remove(viewModel)
        } else {
            recyclerView?.post { viewmodels.remove(viewModel) }
        }
        onDataChange(size())
    }

    open fun remove(index: Int) {
        if (index < 0 || index >= size()) {
            return
        }
        if (canHandleData()) {
            viewmodels.removeAt(index)
        } else {
            recyclerView?.post { viewmodels.removeAt(index) }
        }
        onDataChange(size())
    }

    open fun clear() {
        if (canHandleData()) {
            viewmodels.clear()
        } else {
            recyclerView?.post { viewmodels.clear() }
        }
        onDataChange(size())
    }

    private fun canHandleData(): Boolean {
        return !(recyclerView?.isComputingLayout ?: true)
    }

    open fun onDataChange(size: Int) {

    }

    /**
     * 绑定recyclerview
     * @param recyclerView RecyclerView?
     */
    open fun bindRecyclerView(recyclerView: RecyclerView?) {
        this@ListAdapter.recyclerView = recyclerView
    }

}
