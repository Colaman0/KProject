package com.kyle.colman.impl

/**
 * Author   : kyle
 * Date     : 2019/10/22
 * Function : list操作类
 */
interface Lister<T> {

    fun initList(): MutableList<T>

    fun add(t: T, index: Int = 0) {
        initList().add(t)
    }

    fun addAll(ts: List<T>, index: Int = 0) {
        initList().addAll(ts)
    }

    fun remove(t: T) {
        if (initList().isNotEmpty()) {
            initList().remove(t)
        }
    }

    fun removeAt(index: Int) {
        if (initList().isNotEmpty() && index in 0..initList().size - 1) {
            initList().removeAt(index)
        }
    }

    fun clear() {
        initList().clear()
    }


}