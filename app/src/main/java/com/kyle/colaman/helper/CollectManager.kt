package com.kyle.colaman.helper

import android.util.SparseArray
import com.blankj.utilcode.util.LogUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colman.helper.EmptyCallLiveData

/**
 * Author   : kyle
 * Date     : 2020/5/20
 * Function : 收藏管理类
 */
object CollectManager {
    val articleCollectMap = SparseArray<EmptyCallLiveData<Boolean>?>()

    fun getCollectLiveDataById(id: Int): EmptyCallLiveData<Boolean>? {
        return articleCollectMap[id]
    }

    fun putNewArticle(id: Int, collect: Boolean) {
        val livedata = articleCollectMap[id]
        if (livedata != null) {
            if (livedata.value != collect) {
                livedata.postValue(collect)
            }
        } else {
            articleCollectMap.put(id, EmptyCallLiveData(collect) {
                articleCollectMap.remove(id)
            })
        }

    }

    suspend fun collect(id: Int) {
        Api.collectArticle(id)
        getCollectLiveDataById(id)?.postValue(true)
    }


    suspend fun unCollect(id: Int) {
        Api.unCollectArticle(id)
        getCollectLiveDataById(id)?.postValue(false)
    }

}