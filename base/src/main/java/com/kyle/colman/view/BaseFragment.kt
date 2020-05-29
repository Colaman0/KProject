package com.kyle.colman.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.colaman.statuslayout.StatusLayout
import com.gyf.barlibrary.ImmersionBar
import com.kyle.colman.R

/**
 * Author   : kyle
 * Date     : 2020/5/29
 * Function : base fragment
 */
open class KFragment<B : ViewDataBinding>(
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
        var rootView: View?
        if (needStatusLayout) {
            statusLayout = StatusLayout.init(context!!, contentLayoutId)
            binding = DataBindingUtil.findBinding<B>(statusLayout!!)!!
            rootView = statusLayout
        } else {
            rootView = inflater.inflate(contentLayoutId, container, false)
            binding = DataBindingUtil.findBinding<B>(rootView)!!
        }
        return rootView
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
}