package com.kyle.colman.config

import android.app.Activity
import androidx.paging.PagingSource
import com.kyle.colman.recyclerview.PagingAdapter
import com.kyle.colman.recyclerview.PagingItemView
import java.io.Serializable

/**
 * Author   : kyle
 * Date     : 2020/7/2
 * Function : 列表activity配置
 */

data class PageViewConfig<T : Any>(
    val source: () -> PagingSource<*, *>,
    val uiTrans: (
        T, PagingAdapter
    ) -> PagingItemView<*, *>
) : Serializable