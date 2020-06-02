package com.kyle.colman.manager

import com.kyle.colman.helper.PermissionHelper
import com.kyle.colman.impl.IPermissions

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/8
 *     desc   : 作为权限申请管理类，通过这个类来获取一个IPermissions来加载图片，方便切换不同的库
 *              注意的是不同图片加载库都需要实现IPermissions这个接口
 * </pre>
 */
object PermissionManager {

    private val mPermission by lazy {
        PermissionHelper
    }

    /**
     *  获取图片加载器
     */
    fun getPermissionHelper(): IPermissions = mPermission
}