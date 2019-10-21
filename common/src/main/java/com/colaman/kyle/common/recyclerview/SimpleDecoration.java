package com.colaman.kyle.common.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by kyle on 2018/8/29
 * Function : 分割线，适用于Linearlayoutmanager
 */
public class SimpleDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int MATCH_PARENT = -1;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();
    private int mWidth;
    private int mHeight;
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;
    private Context mContext;


    public SimpleDecoration(Context context, int orientation) {
        mContext = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                    + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        setOrientation(orientation);
    }

    public SimpleDecoration(Context context, int orientation, Drawable drawable) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                    + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        setOrientation(orientation);
        setDrawable(drawable);
    }

    public SimpleDecoration setWidth(@DimenRes int width) {
        mWidth = getPx(width);
        return this;
    }

    public SimpleDecoration setHeight(@DimenRes int height) {
        mHeight = getPx(height);
        return this;
    }


    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public SimpleDecoration setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
        return this;
    }

    public SimpleDecoration setColor(@ColorRes int colorRes) {
        mDivider = new ColorDrawable(mContext.getResources().getColor(colorRes));
        return this;
    }

    public Drawable getDivider() {
        return mDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = getLeft();
            right = parent.getWidth() - getRight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - getDrawHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = getTop();
            bottom = parent.getHeight() - getBottom();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - getDrawWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            outRect.set(getLeft(), isFirst(view, parent), getRight(), getDrawHeight());
        } else {
            outRect.set(isFirst(view, parent), getTop(), getDrawHeight(), getBottom());
        }
    }


    protected int getDrawHeight() {
        return mHeight > 0 ? mHeight : mDivider.getIntrinsicHeight();
    }

    private int getDrawWidth() {
        return mWidth > 0 ? mWidth : mDivider.getIntrinsicWidth();
    }

    public int getRight() {
        return mRight;
    }

    public SimpleDecoration setRight(@DimenRes int right) {
        mRight = getPx(right);
        return this;
    }

    public int getLeft() {
        return mLeft;
    }

    public SimpleDecoration setLeft(@DimenRes int left) {
        mLeft = getPx(left);
        return this;
    }

    public int getBottom() {
        return mBottom;
    }

    public SimpleDecoration setBottom(@DimenRes int bottom) {
        mBottom = getPx(bottom);
        return this;
    }

    public int getTop() {
        return mTop;
    }

    public SimpleDecoration setTop(@DimenRes int top) {
        mTop = getPx(top);
        return this;
    }

    private int getPx(@DimenRes int res) {
        if (mContext != null) {
            return mContext.getResources().getDimensionPixelOffset(res);
        }
        return 0;
    }

    private int isFirst(View view, RecyclerView parent) {
        if (parent.getChildAdapterPosition(view) == 0) {
            return mTop;
        }
        return 0;
    }
}
