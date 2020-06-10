package com.kyle.colaman.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colman.others.StateLiveData
import com.kyle.colman.others.StateObserver
import com.kyle.colman.others.stateLivedata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系viewmodel
 */
class TixiViewModel : ViewModel() {
    val tixiItems = StateLiveData<List<TixiEntity>>()
    var firstItem = ObservableField<String>("")
    var secondItem = ObservableField<String>("")
    var lastId: MutableLiveData<Int> = MutableLiveData<Int>()

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    suspend fun getTixi() {
//        liveData<> {  }
//        Api.getTixi()
//            .bindRxLivedata(tixiItems)
//            .fullSubscribe()
        val flow = flow {
            emit(Api.getTixi()!!)

        }.catch { }.flowOn(Dispatchers.IO)

        flow
            .onEach { }
            .catch { }
            .asLiveData()

    }

    fun getArticle(id: Int) {
        lastId.postValue(id)
    }
}