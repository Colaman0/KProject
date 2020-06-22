package com.kyle.colman.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kyle.colman.view.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import com.kyle.colman.R

/**
 * Author   : kyle
 * Date     : 2020/5/29
 * Function : base fragment
 */
abstract class KFragment<B : ViewDataBinding>(
    val contentLayoutId: Int,
    val needStatusLayout: Boolean = false,
    @ColorRes
    val statusbarColor: Int = R.color.white
) : Fragment() {
    var statusLayout: StatusLayout? = null
    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View?
        val view = layoutInflater.inflate(contentLayoutId, container, false)
        if (needStatusLayout) {
            statusLayout = StatusLayout.init(view)
            binding = DataBindingUtil.bind(view)!!
            rootView = statusLayout
        } else {
            rootView = view
            binding = DataBindingUtil.bind(view)!!
        }
        binding.lifecycleOwner = this
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        getKActivity()?.mImmersionBar?.barColor(statusbarColor)
    }

    fun getKActivity(): KActivity<*>? {
        if (activity is KActivity<*>) {
            return activity as KActivity<*>
        }
        return null
    }

    abstract fun initView()

}