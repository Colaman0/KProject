package com.colaman.kyle.common.helper

import androidx.databinding.ObservableBoolean
import com.colaman.kyle.common.Lister
import com.colaman.kyle.impl.ISelect

/**
 * Author   : kyle
 * Date     : 2019/10/22
 * Function : 全选/单选的辅助类
 */

class SelectHelper private constructor(
    val type: SELECT_TYPE,
    val doubleClickCancel: Boolean
) : Lister<ISelect> {
    val datas = mutableListOf<ISelect>()
    val selectHelpers = mutableListOf<SelectHelper>()

    override fun initList() = datas

    val selectors = mutableListOf<ISelect>()

    companion object {
        fun getInstance(
            type: SELECT_TYPE = SELECT_TYPE.SINGLE,
            doubleClickCancel: Boolean = false
        ) =
            SelectHelper(type, doubleClickCancel)
    }

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
    fun check(index: Int) = check(initList()[index])

    fun check(data: ISelect) {
        if (doubleClickCancel) {
            data.status.set(!data.status.get())
        } else {
            data.status.set(true)
        }
        data.onChange(data.status.get())
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