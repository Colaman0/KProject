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
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/5
 * Function : 主页viewmodel
 */
class MainViewModel : BaseViewModel(), Serializable {

}