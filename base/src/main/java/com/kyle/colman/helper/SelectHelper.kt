package com.kyle.colman.helper

import androidx.databinding.ObservableBoolean
import com.kyle.colman.impl.ISelect
import com.kyle.colman.impl.Lister

/**
 * Author   : kyle
 * Date     : 2019/10/22
 * Function : 全选/单选的辅助类
 */

class SelectHelper(
        val type: SELECT_TYPE,
        val doubleClickCancel: Boolean = false
) : Lister<ISelect> {
    val datas = mutableListOf<ISelect>()
    private val selectHelpers = mutableListOf<SelectHelper>()
    var lastSelectData: ISelect? = null

    override fun initList() = datas

    /**
     * 把所有数据的选中状态反转
     */
    fun reverse() {
        datas.forEach {
            it.status.set(!it.status.get())
            it.onChange(it.status.get())
        }
        selectHelpers.forEach {
            it.reverse()
        }
    }

    /**
     * 选中所有
     */
    fun selectAll() {
        datas.forEach {
            it.status.set(true)
            it.onChange(it.status.get())
        }
        selectHelpers.forEach {
            it.selectAll()
        }
    }

    /**
     * 取消选中所有
     */
    fun unselectAll() {
        datas.forEach {
            it.status.set(false)
            it.onChange(it.status.get())
        }
        selectHelpers.forEach {
            it.unselectAll()
        }
    }

    /**
     * 选中,根据情况决定是否多次点击翻转状态
     */
    fun check(index: Int) = if (type == SELECT_TYPE.SINGLE) singleCheck(datas[index]) else mutliCheck(datas[index])

    fun mutliCheck(data: ISelect) {
        if (doubleClickCancel) {
            data.status.set(!data.status.get())
        } else {
            data.status.set(true)
        }
        data.onChange(data.status.get())
    }

    fun singleCheck(data: ISelect) {
        if (data == lastSelectData) {
            if (doubleClickCancel) {
                data.status.set(!data.status.get())
                data.onChange(data.status.get())
            } else {
                data.status.set(true)
                data.onChange(data.status.get())
            }
        } else {
            lastSelectData?.status?.set(false)
            lastSelectData?.onChange(false)
            data.status.set(true)
            data.onChange(true)
        }
        lastSelectData = data
    }

    fun bindSelectHelper(vararg helpers: SelectHelper): SelectHelper {
        this.selectHelpers.addAll(helpers)
        return this
    }
}

val ISelect.status by lazy {
    return@lazy ObservableBoolean(false)
}

enum class SELECT_TYPE {
    SINGLE, MUTLIPE
}