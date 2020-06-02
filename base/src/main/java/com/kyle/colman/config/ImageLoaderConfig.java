package com.kyle.colman.config;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

/**
 * Create by kyle on 2019/1/7
 * Function : ImageLoader的配置
 */
public class ImageLoaderConfig {
    private Drawable mLoadingDrawable;
    private Drawable mErrorDrawable;
    private Drawable mDefaultDrawable;
    private @DrawableRes
    int mLoadingRes;
    private @DrawableRes
    int mErrorRes;
    private @DrawableRes
    int mDefaultRes;
    private Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    public Drawable getLoadingDrawable() {
        return mLoadingDrawable;
    }

    public ImageLoaderConfig setLoadingDrawable(Drawable loadingDrawable) {
        mLoadingDrawable = loadingDrawable;
        return this;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public ImageLoaderConfig setErrorDrawable(Drawable errorDrawable) {
        mErrorDrawable = errorDrawable;
        return this;
    }

    protected Drawable getDrawable(@DrawableRes int drawableRes) {
        return mContext.getResources().getDrawable(drawableRes);
    }

    public int getLoadingRes() {
        return mLoadingRes;
    }

    public void setLoadingRes(int loadingRes) {
        mLoadingRes = loadingRes;
    }

    public int getErrorRes() {
        return mErrorRes;
    }

    public void setErrorRes(int errorRes) {
        mErrorRes = errorRes;
    }

    public int getDefaultRes() {
        return mDefaultRes;
    }

    public void setDefaultRes(int defaultRes) {
        mDefaultRes = defaultRes;
    }

    public Drawable getDefaultDrawable() {
        return mDefaultDrawable;
    }

    public void setDefaultDrawable(Drawable defaultDrawable) {
        mDefaultDrawable = defaultDrawable;
    }
}
