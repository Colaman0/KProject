package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colman.view.recyclerview.RecyclerItemView
import com.kyle.colman.impl.OnItemClickListener

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/29
 *     desc   :
 * </pre>
 */
open class BaseRecyclerViewAdapter(
    context: Context?,
    data: MutableList<RecyclerItemView<out ViewDataBinding, out Any>?> = mutableListOf()
) : ListAdapter<BaseViewHolder<ViewDataBinding>>(context, data) {
    var lifecycleOwner: LifecycleOwner? = null
    var clickCallback = mutableListOf<OnItemClickListener>()

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    init {
        if (context is LifecycleOwner) {
            bindLifeCycleOwner(context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.layoutRes ?: -1
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        val data = getItem(position)
        if (data != null) {
            /**
             * 把对应的viewmodel绑定到viewholder上
             */
            holder.bindViewModel(data as RecyclerItemView<ViewDataBinding, Any>, position)
        }
    }


    open fun getItem(position: Int) = viewmodels[position]

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding> {
        Log.d("cola","onCreateViewHolder")
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
                if (viewmodel != null && viewmodel is RecyclerItemView<*, *>) {
                    viewmodel.onViewDetached()
                }
            }

            override fun onChildViewAttachedToWindow(view: View) {
                val viewmodel = view.tag
                if (viewmodel != null && viewmodel is RecyclerItemView<*, *>) {
                    viewmodel.onViewAttached()
                }
            }
        })
    }
}