package com.colaman.kyle.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import io.reactivex.Observable;

/**
 * Create by kyle on 2019/1/w
 * Function : 图片加载框架的接口规范
 */
public interface IImageLoad {
    void loadImage(@NonNull Context context, String url, @NonNull ImageView imageView);

    void loadImage(@NonNull Context context, String url, @NonNull ImageView imageView, @DrawableRes int loadingRes, @DrawableRes int errorRes);

    void loadCircleImage(@NonNull Context context, String url, @NonNull ImageView imageView);

    Observable<Bitmap> downLoadBitmap(Context context, final String source);

    void loadDrawable(@NonNull Context context, @NonNull ImageView imageView, @DrawableRes int drawableRes);

    void loadDrawable(@NonNull Context context, @NonNull ImageView imageView, @NonNull Drawable drawable);
}
