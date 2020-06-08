package com.kyle.colman.impl

/**
 * Create by kyle on 2019/1/7
 * Function : 用于判断两个类是否一致，用于adapter中的diffutils刷新
 * RecyclerItemViewModel默认实现了这个接口，可以根据viewmodel的实体类/其他数据去判断需不需要刷新当前item
 */
interface IDiffComparator<T : Any> {

    fun isUISame(data: T): Boolean

    fun isItemSame(data: T): Boolean

}
