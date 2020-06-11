package com.kyle.colman.view.recyclerview.layoutmanager

import android.content.Context
import android.graphics.PointF
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


/**
 *
 *     author : kyle
 *     time   : 2019/10/14
 *     desc   : 适配LinearLayoutManager
 *
 */
class WrapLinearlayoutManager(
    context: Context,
    orientation: Int,
    reverse: Boolean,
    val duration: Int = 300
) : LinearLayoutManager(context, orientation, reverse) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {

        }
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView, state: RecyclerView.State,
        position: Int
    ) {
        if (itemCount == 0) {
            return
        }
        val firstVisibleChild = recyclerView.getChildAt(0)
        val itemHeight = firstVisibleChild.height
        val currentPosition = recyclerView.getChildLayoutPosition(firstVisibleChild)
        var distanceInPixels = Math.abs((currentPosition - position) * itemHeight)
        if (distanceInPixels == 0) {
            distanceInPixels = Math.abs(firstVisibleChild.y).toInt()
        }
        val smoothScroller =
            SmoothScroller(recyclerView.context, distanceInPixels, duration)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class SmoothScroller(
        context: Context?,
        distanceInPixels: Int,
        duration: Int
    ) :
        LinearSmoothScroller(context) {
        private val distanceInPixels: Float
        private val duration: Float
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@WrapLinearlayoutManager
                .computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateTimeForScrolling(dx: Int): Int {
            val proportion = dx.toFloat() / distanceInPixels
            return (duration * proportion).toInt()
        }

        init {
            this.distanceInPixels = distanceInPixels.toFloat()
            this.duration = duration.toFloat()
        }
    }


}
