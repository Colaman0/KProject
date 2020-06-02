package com.kyle.colman.impl

import com.kyle.colman.view.recyclerview.RecyclerItemView

/**
 * Author   : kyle
 * Date     : 2020/6/2
 * Function : recyclerview 列表数据创建接口
 */
interface IRVDataCreator<T> {
    suspend fun loadDataByPage(page: Int): IPageDTO<T>
    suspend fun dataToItemView(data: T): RecyclerItemView<*, *>
}