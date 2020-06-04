package com.kyle.colman.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.ApiUtils
import com.kyle.colman.view.recyclerview.RecyclerItemView

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : recyclerview 列表数据创建接口
 */
interface IRVDataCreator<T> {
    fun loadDataByPage(page: Int): LiveData<IPageDTO<T>>
    fun dataToItemView(data: T): RecyclerItemView<*, *>
}