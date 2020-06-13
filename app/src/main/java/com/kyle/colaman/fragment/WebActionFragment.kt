package com.kyle.colaman.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kyle.colaman.R
import com.kyle.colaman.copyToBorad
import com.kyle.colaman.databinding.LayoutWebBottomActionBinding
import com.kyle.colaman.helper.CollectManager
import com.kyle.colman.config.Constants
import com.kyle.colman.helper.FilterTime
import com.kyle.colman.helper.kHandler
import com.kyle.colman.view.CommonDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/6/13
 * Function : web页面actionfragment
 */
class WebActionFragment : BottomSheetDialogFragment() {
    private var articleId = 0
    private var url = ""
    private val loadingDialog by lazy {
        CommonDialog(context!!)
    }
    val collectLivedata by lazy {
        CollectManager.getCollectLiveDataById(articleId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_web_bottom_action, container, false)
        val binding = DataBindingUtil.bind<LayoutWebBottomActionBinding>(view)
        binding?.fragment = this
        binding?.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            articleId = arguments!!.getInt(Constants.DATA)
            url = arguments!!.getString(Constants.URL).toString()
        }
    }

    companion object {
        fun newInstance(id: Int, url: String): WebActionFragment {
            val args = Bundle()
            args.putInt(Constants.DATA, id)
            args.putString(Constants.URL, url)
            val fragment = WebActionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun actionClick(view: View) {
        when (view.id) {
            R.id.layout_collect -> collect()
            R.id.layout_link -> copyLink()
            R.id.layout_broswer -> openBrowser()
            R.id.layout_pocket -> inPocket()
        }
    }

    @FilterTime(500)
    fun collect() {
        loadingDialog.show()
        lifecycleScope.launch(Dispatchers.IO + kHandler {
            loadingDialog.dismiss()
            dismiss()
        }) {
            if (collectLivedata?.value!!) {
                CollectManager.unCollect(articleId)
            } else {
                CollectManager.collect(articleId)
            }
            loadingDialog.dismiss()
            dismiss()
        }
    }

    fun copyLink() {
        context?.let { copyToBorad(it, url) }
        ToastUtils.showShort("链接已复制")
    }

    fun inPocket() {}

    fun openBrowser() {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}