package com.kyle.colaman.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colman.network.ApiException
import com.kyle.colman.others.*
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

}