package com.colaman.common.imp

import android.content.Context
import com.yanzhenjie.permission.AndPermission

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/6/8
 *     desc   : 权限申请的接口，权限管理类需要实现所有方法，以便于app切换申请库或方法时避免所有地方都要调整
 * </pre>
 */
typealias result = (Boolean) -> Unit

interface IPermissions {
    /**
     * 申请权限
     *
     * @param context     上下文
     * @param permissions 需要的权限
     * @param listener    结果回调
     */
    fun requestPermission(context: Context, vararg permissions: String, listener: result)


    /**
     * 申请权限
     *
     * @param context     上下文
     * @param permissions 需要的权限组
     */
    fun requestPermission(
            context: Context,
            vararg permissions: Array<String>,
            listener: result
    )


    /**
     * 查看权限是否被允许
     *
     * @param context
     * @param permissions
     * @return
     */
    fun checkHasPermission(context: Context, vararg permissions: Array<String>): Boolean

    /**
     * 查看权限是否被允许
     *
     * @param context
     * @param permissions
     * @return
     */
    fun checkHasPermission(context: Context, vararg permissions: String): Boolean

    /**
     * 查看权限是否被允许
     *
     * @param context
     * @param permissions
     * @return
     */
    fun checkHasPermission(context: Context, permissions: List<String>): Boolean {
        return AndPermission.hasPermissions(context, *permissions.toTypedArray())
    }


    /**
     * 弹出一个dialog，提醒用户去设置页打开权限
     *
     * @param context
     * @param listener
     * @param permission
     */
    fun showSettingDialog(context: Context, listener: result, permission: List<String>)

    /**
     * 跳到设置页面
     *
     * @param context
     * @param listener
     * @param permission
     */
    fun gotoSetting(context: Context, listener: result, permission: List<String>)


}