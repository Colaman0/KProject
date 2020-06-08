package com.kyle.colman.helper

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kyle.colman.view.recyclerview.layoutmanager.WrapLinearlayoutManager
import com.kyle.colman.view.recyclerview.adapter.BaseRecyclerViewAdapter
import java.util.concurrent.TimeUnit


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
fun View.singleClick(time: Int = 500, action: () -> Unit): View {
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
 * 设定snackbar多久之后dismiss
 * @receiver Snackbar
 * @param time Int
 * @param unit TimeUnit
 * @return Snackbar
 */
fun Snackbar.durationTime(time: Int, unit: TimeUnit): Snackbar {
    untilTime(unit.toMillis(time.toLong()) + System.currentTimeMillis())
    return this
}


/**
 * 在规定时间后把snackbar关掉
 * @receiver Snackbar
 * @param endTime Long
 * @return Snackbar
 */
fun Snackbar.untilTime(endTime: Long): Snackbar {
    TimeHelper.globalTimer
        .takeUntil { it > endTime }
        .doFinally {
            dismiss()
        }
        .subscribe()
    return this
}


/**
 * 带有动画隐藏view
 * @receiver View
 */
fun View.hide() {
    val animation = AlphaAnimation(1f, 0f)
        .apply {
            duration = 500
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    visibility = View.GONE
                    isEnabled = false
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
    startAnimation(animation)
}

/**
 * 带有动画展示view
 * @receiver View
 */
fun View.visible() {
    val animation = AlphaAnimation(0f, 1f)
        .apply {
            duration = 500
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    visibility = View.VISIBLE
                    isEnabled = true
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
    startAnimation(animation)
}


/**
 * 用于Edittext的内容改变[textChange]
 */
interface onTextChangeListener {
    fun onTextChange(text: String)
}