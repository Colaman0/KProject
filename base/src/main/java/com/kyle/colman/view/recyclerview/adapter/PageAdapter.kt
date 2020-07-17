package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import com.kyle.colman.helper.isNotNullOrEmpty
import com.kyle.colman.impl.*

/**
 * Author   : kyle
 * Date     : 2020/7/16
 * Function : 分页adapter
 */

class PageAdapter<T>(context: Context) : CommonAdapter(context = context) {
    private lateinit var currentSource: IPageSource<*>
    private val loadResultCallback = mutableListOf<(RESULT) -> Unit>()

    fun <T> bindPageSource(source: IPageSource<T>) {
        this.currentSource = source
        source.listen {
            when (it) {
                is PageResult.SUCCESS<*> -> {
                    updateUI(it, source)
                }
                is PageResult.ERROR -> {
                    loadResultCallback.forEach { it.invoke(FAILED) }
                }
            }
        }
    }

    fun <T> updateUI(data: PageResult.SUCCESS<*>, source: IPageSource<T>) {
        if (data.nextPage != null) {
            if (data.data.isNotNullOrEmpty()) {
                getEditableItems().addAll(data.data.map { source.resultToPageItem(it as T) })
                diffNotify()
            }
        } else {
            disableLoadmore(false)
        }
        loadResultCallback.forEach { it.invoke(COMPLETED) }
    }

    override fun onLoadmore() {
        super.onLoadmore()
        currentSource.loadNextPage()
    }

    fun refresh() {
        currentSource.refresh()
    }

    fun addLoadResultCallback(callback: (RESULT) -> Unit) {
        loadResultCallback.add(callback)
    }
}