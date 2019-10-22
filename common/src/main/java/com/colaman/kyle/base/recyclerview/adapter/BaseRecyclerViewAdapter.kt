package com.colaman.kyle.base.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.colaman.kyle.common.recyclerview.RecyclerItemViewModel
import com.colaman.kyle.impl.OnItemClickListener

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/29
 *     desc   :
 * </pre>
 */
open class BaseRecyclerViewAdapter : ListAdapter<BaseViewHolder> {
    var lifecycleOwner: LifecycleOwner? = null
    var clickCallback = mutableListOf<OnItemClickListener>()

    val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    constructor(
        context: Context?,
        datas: MutableList<RecyclerItemViewModel<out ViewDataBinding, out Any>?> = mutableListOf()
    ) : super(context, datas) {
        if (context is LifecycleOwner) {
            bindLifeCycleOwner(context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.initLayouRes() ?: -1
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            /**
             * 把对应的viewmodel绑定到viewholder上
             */
            holder.bindViewModel(data, position)
        }
    }


    open fun getItem(position: Int) = viewmodels[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        val holder = BaseViewHolder(
            context = context,
            view = binding.root,
            binding = binding,
            lifecycleOwner = lifecycleOwner,
            itemType = viewType,
            viewGroup = parent
        )
        /**
         * 注册点击监听
         */
        clickCallback.forEach {
            holder.registerClick(it)
        }
        return holder
    }

    override fun getItemCount() = viewmodels.size


    /**
     * 绑定Lifecycle
     *
     * @param lifecycle 传入外部的Lifecycle
     */
    fun bindLifeCycleOwner(lifecycleOwner: LifecycleOwner?) {
        this@BaseRecyclerViewAdapter.lifecycleOwner = lifecycleOwner
    }


    /**
     * 注册item点击事件
     * @param listener OnItemClickListener 回调
     */
    fun registerClick(listener: OnItemClickListener) {
        clickCallback.add(listener)
    }

    /**
     * 绑定recyclerview的时候捕获滑动事件
     * @param recyclerView RecyclerView?
     */
    override fun bindRecyclerView(recyclerView: RecyclerView?) {
        super.bindRecyclerView(recyclerView)
        catchRecyclerViewScroll()
    }


    /**
     * 捕获recyclerview的滑动事件，给对应的viewmodel回调[onViewDetached] / [onViewAttached]
     *
     */
    protected fun catchRecyclerViewScroll() {

        recyclerView?.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                val viewmodel = view.tag
                if (viewmodel != null && viewmodel is RecyclerItemViewModel<*, *>) {
                    viewmodel.onViewDetached()
                }
            }

            override fun onChildViewAttachedToWindow(view: View) {
                val viewmodel = view.tag
                if (viewmodel != null && viewmodel is RecyclerItemViewModel<*, *>) {
                    viewmodel.onViewAttached()
                }
            }
        })
    }
}