package com.kyle.colman.view.recyclerview

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kyle.colaman.base.viewmodel.BindingViewModel
import com.kyle.colman.view.recyclerview.adapter.BaseViewHolder
import com.kyle.colman.impl.IDiffComparator
import com.kyle.colman.impl.OnItemClickListener

/**
 * Create by kyle on 2018/12/24
 * Function : recyclerview的itemviewmodel ,继承自[BindingViewModel],可以用databinding
 *
 */
abstract class RecyclerItemView<B : ViewDataBinding, VM : Any>(val layoutRes: Int) :
    BindingViewModel<B>(),
    IDiffComparator<VM>,
    OnItemClickListener {
    /**
     * viewmodel默认是显示的，特殊情况可以设置成false，让viewmodel不可见
     */
    var isVisible = true
        set(value) {
            if (value != field && binding != null) {
                /**
                 * 设置根布局的可见性
                 */
                binding?.root?.visibility = if (value) View.VISIBLE else View.GONE
                if (value) {
                    binding?.root?.layoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
                } else {
                    binding?.root?.layoutParams?.height = 0
                }
            }
            field = value
        }

    var isAttached = false
    private var mHolder: BaseViewHolder<B>? = null


    override fun setViewDatabinding(binding: ViewDataBinding?) {
        super.setViewDatabinding(binding)
        /**
         * 设置根布局的可见性
         */
        binding?.root?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    /**
     * 具体view的操作代码写在这个方法内
     */
    abstract fun onBindView(holder: BaseViewHolder<B>)


    /**
     * 判断当前viewmodel在屏幕上是否被attached，也可以认为当前viewmodel可见,[onViewDetached]为不可见
     *
     */
    open fun onViewAttached() {
        isAttached = true
    }

    open fun onViewDetached() {
        isAttached = false
    }


    /**
     * 当前item被点击时的回调
     * @param position Int viewmodel在adapter中的位置
     * @param itemView View viewmodel的根view
     */
    override fun onItemClick(position: Int, itemView: View?) {
    }

    override fun isSame(data: VM): Boolean = false


}
