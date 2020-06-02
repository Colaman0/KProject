package com.kyle.colman.helper

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.SyncStateContract
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.kyle.colman.impl.IVersion
import com.kyle.colman.config.Constants
import com.kyle.colman.view.CommonDialog
import com.kyle.colman.view.KActivity
import io.reactivex.Observable
import java.io.File

/**
 *
 *     author : kyle
 *     time   : 2019/10/25
 *     desc   : Apk版本更新辅助类
 *
 */
object VersionHelper {

    const val REQUEST_CODE = 201

    /**
     * 检查版本
     * @param activity BaseActivity<*> 当前页面activity
     * @param api Observable<IVersion> 检查版本的retrofit流
     * @param dialog Dialog?    提示dialog
     * @param callback Function0<Unit>?  安装失败回调
     */
    fun checkVersion(
        api: Observable<IVersion>,
        activity: KActivity<*>? = ActivityUtils.getTopActivity() as KActivity<*>?,
        dialog: Dialog? = CommonDialog(activity!!),
        callback: (() -> Unit)? = null
    ) {
        var fileName = ""
        dialog?.show()
//        api
//            .filter {
//                // 判断是否需要更新
//                it.needUpdate()
//            }
//            .flatMap {
//                fileName = it.fileName()
//                CommonApi.download(it.apkUrl())
//            }
//            .doOnNext {
//                // Apk文件名
//                val apkFile = File(
//                    PathUtils.getExternalDownloadsPath() +
//                            fileName +
//                            ".apk"
//                )
//                // 写入文件
//                FileIOUtils.writeFileFromIS(apkFile, it.byteStream())
//                activity?.startActivityForResult(getInstallIntent(file = apkFile), REQUEST_CODE)
////                activity?.registerActivityResult { requestCode, resultCode, data ->
////                    // 安装失败回调，安装成功App已经被杀掉，不会收到回调
////                    if (requestCode == REQUEST_CODE) {
////                        callback?.invoke()
////                    }
////                }
//            }
//            .doFinally {
//                dialog?.dismiss()
//            }
//            .fullSubscribe()
    }


    /**
     * 获取跳转到安装页面的intent
     * @param file File 安装包
     * @return Intent
     */
    fun getInstallIntent(file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            val authority = Constants.PATH.FILE_PROVIDER_PATH
            data = FileProvider.getUriForFile(Utils.getApp(), authority, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        }
        Utils.getApp().grantUriPermission(
            Utils.getApp().packageName,
            data,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        intent.setDataAndType(data, type)
        return intent
    }

}