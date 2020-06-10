package com.kyle.colaman.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colman.network.ApiException
import com.kyle.colman.others.StateLiveData
import com.kyle.colman.others.bindLivedata
import com.kyle.colman.others.emitSuccess
import com.kyle.colman.others.stateLivedata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : 主页viewmodel
 */
class MainViewModel : BaseViewModel() {
    val id = MutableLiveData(1)

    val livedata = StateLiveData<Unit>()


    val info = id.switchMap {
        stateLivedata<String>(Dispatchers.IO) {
            if (it > 10) {
                throw ApiException(code = 111, message = "超过10了！")
            }
            emitSuccess("刷新了数据  $it")
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun reload() = Api.test().bindLivedata(liveData = livedata)
}