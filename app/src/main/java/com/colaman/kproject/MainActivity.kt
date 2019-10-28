package com.colaman.kproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SnackbarUtils
import com.colaman.kproject.databinding.ActivityMainBinding
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.common.helper.SnackBarHelper
import com.colaman.kyle.view.SnackBarConfig

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
