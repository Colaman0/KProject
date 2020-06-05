package com.kyle.colman.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.ApiUtils
import com.kyle.colman.view.recyclerview.RecyclerItemView
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : recyclerview 列表数据创建接口
 */
interface IRVDataCreator<T> : Serializable {
    suspend fun loadDataByPage(page: Int): IPageDTO<T>
    suspend fun dataToItemView(data: T): RecyclerItemView<*, *>
}