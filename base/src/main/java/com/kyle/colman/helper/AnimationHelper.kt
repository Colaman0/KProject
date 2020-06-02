package com.kyle.colman.helper

import android.view.View

/**
 * Create by kyle on 2019/1/11
 * Function : 动画辅助类
 */
object AnimationHelper {
    fun height(scale: Float, view: View?, duration: Long) {
        if (view == null || scale <= 0) {
            return
        }
        view.animate()
            .scaleYBy(scale)
            .setDuration(duration)
            .start()
    }

    fun width(scale: Float, view: View?, duration: Long) {
        if (view == null || scale <= 0) {
            return
        }
        view.animate()
            .scaleXBy(scale)
            .setDuration(duration)
            .start()
    }

    fun size(scale: Float, view: View?, duration: Long) {
        if (view == null || scale <= 0) {
            return
        }
        view.animate()
            .scaleXBy(scale)
            .scaleYBy(scale)
            .setDuration(duration)
            .start()
    }

    fun scrollX(x: Float, view: View?, duration: Long) {
        if (view == null) {
            return
        }
        view.animate()
            .translationXBy(x)
            .setDuration(duration)
            .start()
    }

    fun scrollY(y: Float, view: View?, duration: Long) {
        if (view == null) {
            return
        }
        view.animate()
            .translationYBy(y)
            .setDuration(duration)
            .start()
    }

    fun scroll(x: Float, y: Float, view: View?, duration: Long) {
        if (view == null) {
            return
        }
        view.animate()
            .translationXBy(x)
            .translationXBy(y)
            .setDuration(duration)
            .start()
    }
}