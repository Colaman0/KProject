package com.kyle.colaman.activity

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivityWebBinding
import com.kyle.colaman.fragment.WebActionFragment
import com.kyle.colman.config.Constants
import com.kyle.colman.databinding.LayoutRefreshWebviewBinding
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.bindUrl
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : KActivity<ActivityWebBinding>(R.layout.activity_web) {
    val url by lazy {
        intent.getStringExtra(Constants.DATA)
    }
    val actionFragment by lazy {
        WebActionFragment.newInstance(intent.getIntExtra(Constants.ID, 0), url)
    }

    val title by lazy {
        intent.getStringExtra(Constants.TITLE)
    }

    override fun initView() {
        binding?.activity = this
        web_view.apply {
            bindActivity(this@WebActivity)
        }
        web_view.load(this@WebActivity.url)
    }

    fun showBottomAction() {
        actionFragment.show(supportFragmentManager, "action")
    }
}