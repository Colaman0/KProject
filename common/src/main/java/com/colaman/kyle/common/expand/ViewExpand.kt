package com.colaman.kyle.common.expand

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colaman.kyle.base.recyclerview.adapter.BaseRecyclerViewAdapter
import com.colaman.kyle.entity.Constants
import com.colaman.kyle.common.helper.AnimationHelper
import com.colaman.kyle.common.recyclerview.layoutmanager.WrapLinearlayoutManager

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/7/23
 *     desc   : 存放关于view的拓展方法
 * </pre>
 */


/**
 *
 * 给view加上有间隔的点击事件
 * @param time  点击间隔
 * @param action 点击事件回调
 * @return
 */
fun View.singleClick(time: Int = Constants.View.SINGLE_CLICK_TIME, action: () -> Unit): View {
    var lastClick = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClick == 0L || currentTime - lastClick > time) {
            action.invoke()
            lastClick = currentTime
        }
    }
    return this
}

/**
 * 绑定[LinearLayoutManager]
 *
 * @param context
 * @param recyclerViewAdapter adapter
 * @param isVertical    是否垂直
 * @param isReverse     是否翻转
 */
fun RecyclerView.bindLinearAdapter(
        context: Context,
        recyclerViewAdapter: BaseRecyclerViewAdapter,
        isVertical: Boolean = true,
        isReverse: Boolean = false
) {
    layoutManager = WrapLinearlayoutManager(
        context,
        if (isVertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
        isReverse
    )
    recyclerViewAdapter.bindRecyclerView(this)
    adapter = recyclerViewAdapter
}

/**
 * 绑定[GridLayoutManager]
 *
 * @param context
 * @param recyclerViewAdapter adapter
 * @param spancount  数量
 * @param isVertical    是否垂直
 * @param isReverse     是否翻转
 */
fun RecyclerView.bindGridAdapter(
        context: Context,
        recyclerViewAdapter: BaseRecyclerViewAdapter,
        spancount: Int = 1,
        isVertical: Boolean = true,
        isReverse: Boolean = false,
        spanSizeLookup: ((Int) -> Int)? = null
) {
    val manager = GridLayoutManager(
            context,
            spancount,
            if (isVertical) GridLayoutManager.VERTICAL else GridLayoutManager.HORIZONTAL,
            isReverse
    )
    layoutManager = manager
    if (spanSizeLookup != null) {
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = spanSizeLookup.invoke(position)
        }
    }
    recyclerViewAdapter.bindRecyclerView(this)
    adapter = recyclerViewAdapter
}


fun View.scaleHeight(scale: Float, duration: Long = 500) {
    AnimationHelper.height(scale, this, duration)
}


fun View.scaleWidth(scale: Float, duration: Long = 500) {
    AnimationHelper.width(scale, this, duration)
}

fun View.animateSize(scale: Float, duration: Long = 500) {
    animate()
            .scaleXBy(scale)
            .scaleYBy(scale)
            .setDuration(duration)
            .start()
}

fun View.scrollX(x: Float, duration: Long = 500) {
    AnimationHelper.scrollX(x, this, duration)
}

fun View.scrollY(y: Float, duration: Long = 500) {
    AnimationHelper.scrollY(y, this, duration)
}

fun View.scroll(x: Float, y: Float, duration: Long = 500) {
    animate()
            .translationXBy(x)
            .translationXBy(y)
            .setDuration(duration)
            .start()
}


/**
 * 用于Edittext的内容改变[textChange]
 */
interface onTextChangeListener {
    fun onTextChange(text: String)
}