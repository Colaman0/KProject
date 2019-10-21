package com.colaman.kyle.common.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.colaman.kyle.entity.ImageLoaderConfig;
import com.colaman.kyle.impl.IImageLoad;

import java.util.concurrent.Future;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by kyle on 2019/1/7
 * Function : Glide 图片加载辅助类
 */
public class GlideImageLoader implements IImageLoad {
    // Glide的配置
    private RequestOptions mRequestOptions;
    // ImageLoader的配置
    private ImageLoaderConfig mImageLoaderConfig;

    private GlideImageLoader() {

    }

    public static GlideImageLoader getInstance() {
        return Holder.instance;
    }

    public ImageLoaderConfig getImageLoaderConfig() {
        if (mImageLoaderConfig == null) {
            mImageLoaderConfig = new ImageLoaderConfig();
        }
        return mImageLoaderConfig;
    }

    /**
     * 设置全局通用的属性
     *
     * @param imageLoaderConfig
     */
    public void setGlobalConfig(ImageLoaderConfig imageLoaderConfig) {
        mImageLoaderConfig = imageLoaderConfig;
    }

    private static class Holder {
        private static final GlideImageLoader instance = new GlideImageLoader();
    }

    /**
     * init一个默认的requestOption
     *
     * @return
     */
    private RequestOptions initDefaultOption() {
        return new RequestOptions()
                .centerInside()
                .placeholder(getImageLoaderConfig().getLoadingRes())
                .fallback(getImageLoaderConfig().getDefaultRes())
                .error(getImageLoaderConfig().getErrorRes());
    }

    /**
     * 获取默认的配置
     *
     * @return
     */
    public RequestOptions getDefaultOptions() {
        if (mRequestOptions == null) {
            mRequestOptions = initDefaultOption();
        }
        return mRequestOptions;
    }

    /**
     * 获取一个新的配置
     *
     * @return
     */
    protected RequestOptions getNewOption() {
        return initDefaultOption();
    }

    /**
     * 加载图片,使用全局默认的配置
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, getDefaultOptions());
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param loadingRes 加载中的占位图
     * @param errorRes   错误的占位图
     */
    @Override
    public void loadImage(Context context, String url, ImageView imageView, int loadingRes, int errorRes) {
        loadImage(context, url, imageView, getNewOption().placeholder(loadingRes).error(errorRes));
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadCircleImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, getDefaultOptions().circleCrop());
    }

    /**
     * 下载一个bitmap
     *
     * @param context
     * @param source  bitmap路径
     * @return 会返回一个Observable，这种下载方式是异步的，
     * 同步的调用：{@link #getFutureTarget(Context, String, RequestOptions)}
     */
    @Override
    public Observable<Bitmap> downLoadBitmap(Context context, String source) {
        return Observable.just(source)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(s -> getFutureTarget(context, source, getDefaultOptions()))
                .observeOn(Schedulers.newThread())
                .map(Future::get)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void loadDrawable(Context context, ImageView imageView, int drawableRes) {
        if (imageView == null || context == null) {
            return;
        }
        Glide.with(context).load(drawableRes).apply(getDefaultOptions()).into(imageView);
    }

    /**
     * 加载drawable
     *
     * @param context
     * @param imageView
     * @param drawable
     */
    @Override
    public void loadDrawable(@NonNull Context context, @NonNull ImageView imageView, @NonNull Drawable drawable) {
        Glide.with(context).load(drawable).apply(getDefaultOptions()).into(imageView);
    }

    /**
     * 根据ImageLoaderConfig返回一个用于Glide的配置
     *
     * @param config
     * @return
     */
    @SuppressLint("CheckResult")
    private RequestOptions getOptionsByConfig(ImageLoaderConfig config) {
        if (config == null) {
            return getDefaultOptions();
        }
        return new RequestOptions()
                .placeholder(config.getLoadingRes())
                .error(config.getErrorRes())
                .fallback(config.getDefaultRes());
    }


    /**
     * 下载一个bitmap
     *
     * @param context
     * @param source  bitmap路径
     * @return 会返回一个target，这种下载方式是同步的，
     * 异步的调用：{@link #downLoadBitmap(Context, String)}
     */
    @NonNull
    public FutureTarget<Bitmap> getFutureTarget(Context context, String source, RequestOptions options) {
        return Glide.with(context).asBitmap().load(source).apply(options).submit();
    }

    /**
     * 加载图片，传入自定义的Glide的配置
     *
     * @param context
     * @param url
     * @param imageView
     * @param options   Glide 配置
     */
    public void loadImage(Context context, String url, ImageView imageView, RequestOptions options) {
        if (imageView == null || context == null) {
            return;
        }
        if (options == null) {
            options = getDefaultOptions();
        }
        Glide.with(context).load(url).apply(options).into(imageView);
    }


}
