package com.kyle.colaman.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kyle.colaman.source.SearchSource

/**
 * Author   : kyle
 * Date     : 2020/6/18
 * Function : 搜索viewmodel
 */
class SearchViewModel : ViewModel() {

    fun querySearch(text: String) = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 3, initialLoadSize = 0),
        pagingSourceFactory = { SearchSource(text) }
    ).flow.cachedIn(viewModelScope)

}