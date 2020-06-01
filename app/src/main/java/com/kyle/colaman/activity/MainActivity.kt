package com.kyle.colaman.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.R
import com.kyle.colman.helper.kHandler
import com.kyle.colman.helper.reponse
import com.kyle.colman.network.KReponse
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*

class MainActivity : KActivity<Nothing>(R.layout.activity_main) {

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO + kHandler {
            ToastUtils.showShort("$it")
        }) {
            LogUtils.d(Thread.currentThread().name)
            val data = withContext(Dispatchers.IO) {

                text.text = "长度 ${Api.getTixi().reponse()?.size ?: 0}"
            }

        }
    }
}
