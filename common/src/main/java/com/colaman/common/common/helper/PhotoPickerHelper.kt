package com.colaman.common.common.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.colaman.common.common.manager.PermissionManager
import com.colaman.common.entity.Constants
import com.yanzhenjie.permission.Permission
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy


/**
 * function: 基于知乎matisse的图片选择调用
 *
 *
 * create by kyle on 2018/4/3
 */
object PhotoPickerHelper {
    const val REQUEST_CODE = 0x0001
    fun pickPhoto(context: Context?,
                  maxNum: Int = 1,
                  photoType: Set<MimeType> = MimeType.ofImage(),
                  canCapture: Boolean = true,
                  requestCode: Int = REQUEST_CODE) {
        context?.let { context ->
            PermissionManager.getPermissionHelper().requestPermission(context, Permission.Group.CAMERA, Permission.Group.STORAGE) {
                if (it) {
                    // 调用matisse，传入参数
                    showPhotoPicker(context as Activity, maxNum, photoType, canCapture, requestCode)
                } else {
                    Toast.makeText(context, " we can't user the camera without permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /**
     * show matisse
     *
     * @param activity    上下文
     * @param maxNum      最多选择几张图片
     * @param photoType   筛选出可选的图片类型
     * @param canCapture  是否支持拍照
     * @param requestCode 需要传入一个code，在activityresult里通过code去取选中的图片的路径
     */
    fun showPhotoPicker(activity: Activity?, maxNum: Int, photoType: Set<MimeType>, canCapture: Boolean, requestCode: Int) {
        Matisse.from(activity)
                .choose(photoType)
                .capture(canCapture)
                .captureStrategy(CaptureStrategy(true, Constants.PATH.FILE_PROVIDER_PATH))
                .maxSelectable(maxNum)
                .imageEngine(MyGlideEngine())
                .forResult(requestCode)
    }

    fun resolveIntent(intent: Intent?) = Matisse.obtainPathResult(intent)
}
