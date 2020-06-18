package com.kyle.colaman.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.paging.*
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.helper.TixiCreator
import com.kyle.colman.others.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
     fun initTixiHeader() {
        viewModelScope.launch(Dispatchers.IO) {
            Api.getTixi().onEach {
                tixis.clear()
                tixis.addAll(it)
                refreshArticles(it[0].children?.get(0)?.id ?: 0)
            }.bindLivedata(tixiItems)
        }
    }

    /**
     * 刷新对应体系分类下的文章
     *
     * @param id
     */
    fun refreshArticles(id: Int) {
        lastIdValue = id
        lastId.postValue(id)
    }

    fun getArticlePager(id: Int) = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 3, initialLoadSize = 0),
        pagingSourceFactory = { TixiCreator(id) }
    ).flow.cachedIn(viewModelScope)


    /**
     * 设置当前选中的体系
     *
     * @param firstItem
     * @param secondItem
     */
    fun updateNewItemInfo(firstItem: String, secondItem: String) {
        this.firstItem.set(firstItem)
        this.secondItem.set(secondItem)
    }
}