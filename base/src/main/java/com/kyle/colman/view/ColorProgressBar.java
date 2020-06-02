package com.kyle.colman.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.kyle.colman.R;

import java.lang.reflect.Field;

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/2/14
 *     desc   : 可以修改颜色的progressbar
 * </pre>
 */
public class ColorProgressBar extends ProgressBar {
    public ColorProgressBar(Context context) {
        this(context, null);
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorProgressBar);
        int color = typedArray.getColor(R.styleable.ColorProgressBar_progress_color, ContextCompat.getColor(context, R.color.black));
        typedArray.recycle();
        setProgressColor(color);
    }

    public void setProgressColor(int color) {
        boolean isHorizontal = false;
        // 通过反射获取mMirrorForRtl，mMirrorForRtl在style设置为horizontal的时候会设置为true
        try {
            Field declaredField = getClass().getSuperclass().getDeclaredField("mMirrorForRtl");
            declaredField.setAccessible(true);
            isHorizontal = (boolean) declaredField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isHorizontal) {
            setNormalStyleColor(color);
        } else {
            setHorizontalStyleColor(color);
        }
    }

    /**
     * 设置颜色(横向progressbar样式)
     *
     * @param firstColor
     */
    protected void setHorizontalStyleColor(int firstColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable progressDrawable = getProgressDrawable().mutate();
            progressDrawable.setColorFilter(firstColor, PorterDuff.Mode.SRC_IN);
            setProgressDrawable(progressDrawable);
        } else {
            setProgressTintList(ColorStateList.valueOf(firstColor));
        }
    }

    /**
     * 给progressbar设置颜色(默认样式，圆圈)
     *
     * @param color
     */
    protected void setNormalStyleColor(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable.mutate(), color);
            setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
