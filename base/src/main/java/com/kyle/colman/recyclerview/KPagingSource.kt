package com.kyle.colman.recyclerview

import androidx.paging.PagingSource
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/6/17
 * Function : 实现序列化
 */
abstract class KPagingSource<K : Any, V : Any> : PagingSource<K, V>(), Serializable