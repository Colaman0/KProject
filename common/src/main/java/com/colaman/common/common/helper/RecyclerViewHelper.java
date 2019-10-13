package com.colaman.common.common.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.colaman.common.common.recyclerview.SimpleDecortaion;


/**
 * Create by kyle on 2019/1/4
 * Function : recyclerview 辅助类
 */
public class RecyclerViewHelper {
    private static SparseArray<RecyclerView.RecycledViewPool> mRecycledViewPoolMaps;

    /**
     * 定位到对应的position，item会在recyclerview顶部(不带动画)
     *
     * @param recyclerView 需要操作的recyclerview
     * @param position     要滚动到的position
     */
    public static void scrollToPosition(RecyclerView recyclerView, int position) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && position >= 0 && position < recyclerView.getAdapter().getItemCount()) {
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
                return;
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
                return;
            }
        }
    }

    /**
     * 定位到对应的position，item会在recyclerview中间(不带动画)
     *
     * @param recyclerView 需要操作的recyclerview
     * @param position     要滚动到的position
     */
    public static void scrollToPositionCenter(RecyclerView recyclerView, int position) {
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && position >= 0 && position < recyclerView.getAdapter().getItemCount()) {
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
                View item = layoutManager.getChildAt(0);
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, recyclerView.getHeight() / 2 - item.getMeasuredHeight() / 2);
                return;
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
                View item = layoutManager.getChildAt(0);
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, recyclerView.getHeight() / 2 - item.getMeasuredHeight() / 2);
                return;
            }
        }
    }


    /**
     * 定位到对应position(附带动画)
     *
     * @param recyclerView 需要操作的recyclerview
     * @param position     要滚动的position
     * @param scrollType   最终停止时item的位置策略
     *                     LinearSmoothScroller.SNAP_TO_START 使子视图的左侧或顶部与父视图的左侧或顶部对齐
     *                     LinearSmoothScroller.SNAP_TO_ANY 使子视图的右侧或底部与父视图的右侧面或底部对齐
     *                     LinearSmoothScroller.SNAP_TO_END 根据子视图的当前位置与父布局的关系，决定子视图是否从头到尾跟随。
     *                     如果子视图实际位于RecyclerView的左侧，SNAP_TO_ANY和SNAP_TO_START是没有差别的。
     */
    public static void smoothScrollToPosition(RecyclerView recyclerView, int position, int scrollType) {
        if (recyclerView == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager != null && position >= 0 && position < recyclerView.getAdapter().getItemCount()) {
            layoutManager.startSmoothScroll(getLinearSmoothScroller(recyclerView, position, scrollType));
        }
    }

    /**
     * 获取一个LinearSmoothScroller
     */
    @NonNull
    private static LinearSmoothScroller getLinearSmoothScroller(RecyclerView recyclerView, int position, int scrollType) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return scrollType;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 1f;
            }
        };
        smoothScroller.setTargetPosition(position);
        return smoothScroller;
    }

    /**
     * 获取一个适用于LinearlayoutManager的分隔线
     *
     * @param context
     * @param orientation
     * @return
     */
    public static RecyclerView.ItemDecoration getLinearlayoutItemDecoration(Context context, int orientation) {
        return new SimpleDecortaion();
    }

    /**
     * 获取一个recyclerviewPool
     *
     * @return
     */
    public static RecyclerView.RecycledViewPool getRecyclerViewPool() {
        return new RecyclerView.RecycledViewPool();
    }

    /**
     * 通过tag来找出相同的recyclerviewPool,并且设置
     *
     * @param recyclerView
     */
    public static void setExistRecyclerViewPool(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getTag() != null && recyclerView.getTag() instanceof Integer) {
            Integer tag = (Integer) recyclerView.getTag();
            RecyclerView.RecycledViewPool pool = getRecycledViewPoolMaps().get(tag);
            if (pool == null) {
                pool = getRecyclerViewPool();
                getRecycledViewPoolMaps().put(tag, pool);
            }
            recyclerView.setRecycledViewPool(pool);
        }
    }

    private static SparseArray<RecyclerView.RecycledViewPool> getRecycledViewPoolMaps() {
        if (mRecycledViewPoolMaps == null) {
            mRecycledViewPoolMaps = new SparseArray<>();
        }
        return mRecycledViewPoolMaps;
    }
}
