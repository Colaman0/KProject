package com.kyle.colman.view.recyclerview.adapter


import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.view.recyclerview.RecyclerItemView

/**
 * Create by kyle on 2019/1/9
 * Function : 操作数据源的adapter
 */
abstract class ListAdapter<VH : BaseViewHolder<ViewDataBinding>> : RecyclerView.Adapter<VH> {
    protected open var viewmodels = mutableListOf<RecyclerItemView<out ViewDataBinding, out Any>?>()
    open var recyclerView: RecyclerView? = null
    open var context: Context? = null

    constructor(
        context: Context?,
        data: MutableList<RecyclerItemView<*, *>?> = mutableListOf()
    ) {
        this@ListAdapter.context = context
        viewmodels = data
    }

    open fun getDatas() = viewmodels


    open fun size(): Int {
        return viewmodels.size
    }

    operator open fun get(index: Int): RecyclerItemView<*, *>? {
        return viewmodels[index]
    }


    open fun add(view: RecyclerItemView<out ViewDataBinding, out Any>, index: Int = -1) {
        if (index == -1) {
            viewmodels.add(view)
        } else {
            viewmodels.add(index, view)
        }
        onDataChange(size())
    }

    open fun add(view: RecyclerItemView<out ViewDataBinding, out Any>) {
        viewmodels.add(view)
        onDataChange(size())
    }

    open fun addAll(list: Collection<RecyclerItemView<out ViewDataBinding, out Any>>) {
        viewmodels.addAll(list)
        onDataChange(size())
    }

    open fun addAll(list: Collection<RecyclerItemView<out ViewDataBinding, out Any>>, index: Int = 0) {
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

    open fun remove(view: RecyclerItemView<out ViewDataBinding, out Any>) {
        if (canHandleData()) {
            viewmodels.remove(view)
        } else {
            recyclerView?.post { viewmodels.remove(view) }
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
        viewmodels.clear()
        onDataChange(size())
    }

    private fun canHandleData(): Boolean {
        return true
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
