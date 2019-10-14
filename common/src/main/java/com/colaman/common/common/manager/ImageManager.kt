package com.colaman.common.common.manager

import com.colaman.common.common.helper.GlideImageLoader
import com.colaman.common.impl.IImageLoad

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/8
 *     desc   : 作为图片加载库的管理类，通过这个类来获取一个ImageLoader来加载图片，方便切换不同的库
 *              注意的是不同图片加载库都需要实现IImageLoad这个接口
 * </pre>
 */
object ImageManager {

    private val mImageLoader by lazy {
        GlideImageLoader.getInstance()
    }

    /**
     *  获取图片加载器
     */
    fun getImageLoader(): IImageLoad = mImageLoader
}