package com.colaman.kyle.common.helper

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.colaman.kyle.R
import com.colaman.kyle.imp.IPermissions
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RequestExecutor

/**
 * <pre>
 *     author : kyle
 *     time   : 2019/8/12
 *     desc   : 申请权限辅助类
 * </pre>
 */
typealias result = (Boolean) -> Unit

object PermissionHelper : IPermissions {
    override fun requestPermission(context: Context, vararg permissions: String, listener: result) {
        AndPermission.with(context)
                .runtime()
                .permission(*permissions)
                .rationale(DefaultRationale(listener))
                .onGranted { data ->
                    if (checkHasPermission(context, data)) {
                        listener.invoke(true)
                    } else {
                        listener.invoke(false)
                    }
                }
                .onDenied { data ->
                    // 如果是直接被拒绝并且不弹出申请框的情况下，弹出跳到设置页面的dialog让用户自行打开权限
                    deniedJudgement(data, context, listener)
                }
                .start()
    }

    override fun requestPermission(context: Context, vararg permissions: Array<String>, listener: result) {
        AndPermission.with(context)
                .runtime()
                .permission(*permissions)
                .rationale(DefaultRationale(listener))
                .onGranted { data ->
                    // 用户同意权限，回调成功
                    if (checkHasPermission(context, data)) {
                        listener.invoke(true)
                    } else {
                        listener.invoke(false)
                    }
                }
                .onDenied { data ->
                    // 如果是直接被拒绝并且不弹出申请框的情况下，弹出跳到设置页面的dialog让用户自行打开权限
                    deniedJudgement(data, context, listener)
                }
                .start()
    }

    override fun checkHasPermission(context: Context, vararg permissions: Array<String>): Boolean {
        return AndPermission.hasPermissions(context, *permissions)
    }

    override fun checkHasPermission(context: Context, vararg permissions: String): Boolean {
        return AndPermission.hasPermissions(context, *permissions)
    }

    override fun showSettingDialog(context: Context, listener: result, permission: List<String>) {
        val permissionNames = Permission.transformText(context, permission)
        val message = String.format(context.getString(R.string.setting_dialog_msg), permissionNames)
        AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(context.getString(R.string.setting_dialog_title))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.setting_dialog_positive)
                ) { _, _ ->
                    // 跳转到设置页
                    gotoSetting(context, listener, permission)
                }
                .setNegativeButton(context.getString(R.string.setting_dialog_negative)
                ) { _, _ ->
                    // 用户取消跳转到设置页，直接回调失败
                    listener.invoke(false)
                }.show()
    }

    override fun gotoSetting(context: Context, listener: result, permission: List<String>) {
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback {
                    if (checkHasPermission(context, permission)) {
                        listener.invoke(true)
                    } else {
                        listener.invoke(false)
                    }
                }
                .start()
    }

    /**
     * 当回调了denied方法时，再判断一下权限以及做处理
     *
     * @param data
     * @param context
     * @param listener
     */
    private fun deniedJudgement(data: List<String>, context: Context, listener: result) {
        if (checkHasPermission(context, data)) {
            listener.invoke(true)
            return
        }
        if (AndPermission.hasAlwaysDeniedPermission(context, data)) {
            showSettingDialog(context, listener, data)
        } else {
            // 如果不是直接被拒绝而是手动点拒绝的申请下，直接回调失败
            listener.invoke(false)
        }
    }


    /**
     * 当用户拒绝过一次之后，提醒用户权限的作用
     */
    internal class DefaultRationale(private val requestPermissionListener: result) : Rationale<List<String>> {

        override fun showRationale(context: Context, permissions: List<String>, executor: RequestExecutor?) {
            val permissionNames = Permission.transformText(context, permissions)
            val message = String.format(context.getString(R.string.rationale_dialog_msg), permissionNames)
            // 弹出一个提醒dialog
            AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(context.getString(R.string.rationale_dialog_title))
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.rationale_dialog_positive)
                    ) { _, _ ->
                        // 点击提示框的允许之后重新申请一次权限
                        executor?.execute()
                    }
                    .setNegativeButton(context.getString(R.string.rationale_dialog_negative)
                    ) { _, _ ->
                        // 点击拒绝后回调失败
                        executor?.cancel()
                        requestPermissionListener.invoke(false)
                    }
                    .show()
        }
    }


}