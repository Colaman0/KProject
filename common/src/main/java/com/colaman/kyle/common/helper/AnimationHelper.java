package com.colaman.kyle.common.helper;

import android.view.View;

/**
 * Create by kyle on 2019/1/11
 * Function : 动画辅助类
 */
public class AnimationHelper {
    public static void height(float scale, View view, long duration) {
        if (view == null || scale <= 0) {
            return;
        }
        view.animate()
                .scaleYBy(scale)
                .setDuration(duration)
                .start();
    }

    public static void width(float scale, View view, long duration) {
        if (view == null || scale <= 0) {
            return;
        }
        view.animate()
                .scaleXBy(scale)
                .setDuration(duration)
                .start();
    }

    public static void size(float scale, View view, long duration) {
        if (view == null || scale <= 0) {
            return;
        }
        view.animate()
                .scaleXBy(scale)
                .scaleYBy(scale)
                .setDuration(duration)
                .start();
    }

    public static void scrollX(float x, View view, long duration) {
        if (view == null) {
            return;
        }
        view.animate()
                .translationXBy(x)
                .setDuration(duration)
                .start();
    }

    public static void scrollY(float y, View view, long duration) {
        if (view == null) {
            return;
        }
        view.animate()
                .translationYBy(y)
                .setDuration(duration)
                .start();
    }

    public static void scroll(float x, float y, View view, long duration) {
        if (view == null) {
            return;
        }
        view.animate()
                .translationXBy(x)
                .translationXBy(y)
                .setDuration(duration)
                .start();
    }
}
