package com.colaman.kyle.base.recyclerview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
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
open class BaseViewHolder : RecyclerView.ViewHolder {
    companion object {
        const val TAG = 0x000001
    }

    var binding: ViewDataBinding? = null
    var context: Context? = null
    var lifecycleOwner: LifecycleOwner? = null
    var itemType = 0
    var viewGroup: ViewGroup? = null
    var viewModel: RecyclerItemViewModel<out ViewDataBinding, *>? = null
    var clickCallback = mutableListOf<OnItemClickListener>()
    var currentPosition = -1

    constructor(
            context: Context?,
            view: View,
            binding: ViewDataBinding,
            lifecycleOwner: LifecycleOwner?,
            itemType: Int,
            viewGroup: ViewGroup
    ) : this(view) {

        this@BaseViewHolder.context = context
        this@BaseViewHolder.binding = binding
        this@BaseViewHolder.lifecycleOwner = lifecycleOwner
        this@BaseViewHolder.viewGroup = viewGroup
        this@BaseViewHolder.itemType = itemType
    }

    constructor(view: View) : super(view) {
        // 给整个根view设置点击事件，然后依次回调viewmodel以及外部注册的监听
        view.setOnClickListener { view ->
            viewModel?.onItemClick(currentPosition, view)
            clickCallback.forEach {
                it.onItemClick(currentPosition, view)
            }
        }
    }


    /**
     * 注册根view点击的回调
     * @param listener OnClickListener
     */
    fun registerClick(listener: OnItemClickListener) {
        clickCallback.add(listener)
    }


    /**
     * 给ViewHolder绑定一个[RecyclerItemViewModel] ，具体逻辑都写在viewmodel里
     *
     * @param viewModel
     */
    fun bindViewModel(viewModel: RecyclerItemViewModel<out ViewDataBinding, *>, position: Int) {
        this@BaseViewHolder.viewModel = viewModel
        // 设置tag，以便adapter通过view来获取当前显示的view上绑定的viewmodel
        itemView.tag = viewModel
        currentPosition = position
        viewModel.bindLife(lifecycleOwner)
        /**
         * 给recyclerview的binding赋值
         */
        viewModel.setViewDatabinding(binding)
        /**
         * viewdatabiding需要设置lifecycleOwner  livedata才会更新数据
         */
        viewModel.onBindView(this)
    }
}