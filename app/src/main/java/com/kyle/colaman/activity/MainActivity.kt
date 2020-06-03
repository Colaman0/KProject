package com.kyle.colaman.activity

import android.os.Looper
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.R
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.entity.error.LoginError
import com.kyle.colman.helper.kHandler
import com.kyle.colman.helper.toData
import com.kyle.colman.network.ApiException
import com.kyle.colman.network.ERROR
import com.kyle.colman.network.IExceptionFilter
import com.kyle.colman.network.KError
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_main.*
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
                val isMainThread = Looper.getMainLooper().thread.id == Thread.currentThread().id
                text.text = "主线程  $isMainThread -  ${Api.getTixi().toData()?.size ?: 0}"
            }
        }
    }
}

object LoginFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is ApiException && throwable.code == 1001
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, errorType = LoginError)
    }
}