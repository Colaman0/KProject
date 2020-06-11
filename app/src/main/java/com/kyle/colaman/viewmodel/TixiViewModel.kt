package com.kyle.colaman.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colman.others.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Author   : kyle
 * Date     : 2020/5/15
 * Function : 体系viewmodel
 */
@ExperimentalCoroutinesApi
class TixiViewModel : ViewModel() {
    val tixis = mutableListOf<TixiEntity>()
    var lastIdValue = 0
    val tixiItems = StateLiveData<List<TixiEntity>>()
    var firstItem = ObservableField<String>("")
    var secondItem = ObservableField<String>("")
    var lastId: MutableLiveData<Int> = MutableLiveData<Int>()


    @ExperimentalCoroutinesApi
    suspend fun initTixiHeader() {
        Api.getTixi().onEach {
            tixis.clear()
            tixis.addAll(it)

        }.bindLivedata(tixiItems)
    }

    fun getArticle(id: Int) {
        lastIdValue = id
        lastId.postValue(id)
    }
}