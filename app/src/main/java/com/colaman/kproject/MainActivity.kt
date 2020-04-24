package com.colaman.kproject

import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import androidx.cardview.widget.CardView
import androidx.leanback.widget.ShadowOverlayContainer
import androidx.leanback.widget.ShadowOverlayHelper
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.colaman.kproject.databinding.ActivityMainBinding
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.common.brocast.NetworkManager
import com.colaman.kyle.common.expand.hide
import com.colaman.kyle.common.expand.visible
import com.colaman.kyle.common.helper.SnackBarHelper
import com.colaman.kyle.common.rx.RxLivedata
import com.colaman.kyle.common.rx.bindRxLivedata
import com.colaman.kyle.common.rx.bindStatusImpl
import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.network.NetworkStatusListener
import com.colaman.kyle.view.SnackBarConfig
import com.colaman.kyle.view.StatusToast
import com.colaman.kyle.viewmodel.LifeViewModel
import com.google.android.material.shadow.ShadowDrawableWrapper
import com.google.android.material.shape.MaterialShapeDrawable
import io.reactivex.Observable
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding, LifeViewModel>() {
    override fun createViewModel() = null

    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = SnackBarConfig(
                msg = System.currentTimeMillis().toString()
        )

    }

    fun jump(view: View?) {
//        startActivity(Intent(this, MainActivity::class.java))

    }

    fun show(view: View?) {
        binding.btnShow?.isSelected = true

    }
}
