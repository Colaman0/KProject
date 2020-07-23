package com.kyle.colaman.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colman.network.ApiException
import com.kyle.colman.others.StateLiveData
import com.kyle.colman.others.bindLivedata
import com.kyle.colman.others.emitSuccess
import com.kyle.colman.others.stateLivedata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : 主页viewmodel
 */
class MainViewModel : BaseViewModel(), Serializable {
//    val flow = MutableStateFlow<Int>(0)
//
//    init {
//
//        viewModelScope.launch {
//            repeat(1000) {
//                delay(500)
//                flow.value++
//            }
//        }
//    }
}