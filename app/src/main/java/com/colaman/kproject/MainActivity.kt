package com.colaman.kproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.colaman.kproject.databinding.ActivityMainBinding
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.common.brocast.NetworkManager
import com.colaman.kyle.common.helper.SnackBarHelper
import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.network.NetworkStatusListener
import com.colaman.kyle.view.SnackBarConfig
import io.reactivex.Observable
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = SnackBarConfig(
            msg = System.currentTimeMillis().toString()
        )
        SnackbarUtils.with(window.decorView)
            .run {
                if (!config.actionText.isNullOrBlank()) {
                    setAction(config.actionText!!, config.actionListener!!)
                }
                setBgColor(config.bgColor!!)
                setBgResource(config.bgResource!!)
                setMessage(config.msg!!)
                setDuration(SnackbarUtils.LENGTH_INDEFINITE)
                setMessageColor(config.msgColor!!)
                setDuration(config.duration!!)
            }
            .show()

        NetworkManager.addNetworkListener(object : NetworkStatusListener {
            override fun onNetworkType(type: String) {
                LogUtils.d("网络类型=$type")
            }

            override fun onNetworkChange() {
            }

            override fun onNetworkAvailable(available: Boolean) {
                LogUtils.d("网络畅通=$available")
            }
        })



        Observable.interval(0, 1, TimeUnit.SECONDS)
            .retry()
            .doOnNext {
                if (it > 5) {
                    throw NullPointerException()
                }
                Log.d("cola", "data = $it")
            }
            .subscribe()
    }

    fun jump(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun show(view: View?) {
        SnackBarHelper.push(
            SnackBarConfig(
                msg = System.currentTimeMillis().toString()
            )
        )
    }
}
