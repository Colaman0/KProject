package com.kyle.colman.view.recyclerview.adapter

import android.content.Context
import com.kyle.colman.helper.isNotNullOrEmpty
import com.kyle.colman.impl.*

/**
 * Author   : kyle
 * Date     : 2020/7/16
 * Function : 分页adapter
 */

open class PageAdapter<T>(context: Context) : CommonAdapter(context = context) {
    private lateinit var currentSource: IPageSource<*>
    private val loadResultCallback = mutableListOf<(RESULT) -> Unit>()
    var isRefresh = true
    fun <T> bindPageSource(source: IPageSource<T>) {
        this.currentSource = source
        source.listen {
            when (it) {
                is PageResult.SUCCESS<*> -> {
                    updateUI(it, source)
                }
                is PageResult.ERROR -> {
                    loadResultCallback.forEach { it.invoke(RESULT.FAILED) }
                }
            }
        }
    }

    fun <T> updateUI(data: PageResult.SUCCESS<*>, source: IPageSource<T>) {
        if (isRefresh) {
            getEditableItems().clear()
        }
        if (data.data.isNotNullOrEmpty()) {
            getEditableItems().addAll(data.data.map { source.resultToPageItem(it as T) })
            diffNotify()
        }
        disableLoadmore(data.nextPage != null)
        loadResultCallback.forEach { it.invoke(RESULT.COMPLETED) }
    }

    override fun onLoadmore() {
        super.onLoadmore()
        currentSource.loadNextPage()
    }

    fun refresh() {
        isRefresh = true
        currentSource.refresh()
    }

    fun addLoadResultCallback(callback: (RESULT) -> Unit) {
        loadResultCallback.add(callback)
    }

    fun retry() {
    }
}