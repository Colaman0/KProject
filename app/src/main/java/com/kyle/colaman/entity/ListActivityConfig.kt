package com.kyle.colaman.entity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingSource
import com.kyle.colaman.activity.ListActivity
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.recyclerview.PagingItemView
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/7/2
 * Function : 列表activity配置
 */

data class ListActivityConfig<T : Any>(
    val title: String,
    val source: () -> PagingSource<*, *>,
    val uiTrans: (
        T, PagingAdapter, ListActivity
    ) -> PagingItemView<*, *>
) : Serializable