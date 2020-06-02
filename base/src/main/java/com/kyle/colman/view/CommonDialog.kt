package com.kyle.colman.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.StringUtils.getString

import com.kyle.colman.impl.IStatus
import com.kyle.colman.BR
import com.kyle.colman.R
import com.kyle.colman.databinding.DialogCommonBinding
import kotlinx.android.synthetic.main.dialog_common.*

/**
 *
 *     author : kyle
 *     time   : 2019/10/16
 *     desc   : 通用dialog,实现了IStatus接口
 *
 */
class CommonDialog(context: Context) : Dialog(context, R.style.CommonDialog), IStatus {

    lateinit var mBinding: DialogCommonBinding
    var builder: CommonDialogBuilder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
        setContentView(view)
        mBinding = DataBindingUtil.bind(view)!!
        builder = builder ?: getDefaultBuilder()
        mBinding.setVariable(BR.dialog, builder)
        progress_bar.setProgressColor(ContextCompat.getColor(context, builder!!.progressColor))
    }

    fun setBuilder(builder: CommonDialogBuilder): CommonDialog {
        this.builder = builder
        return this
    }


    fun getDefaultBuilder() = CommonDialogBuilder()

    class CommonDialogBuilder(
        val textColor: Int = R.color.white,
        val text: String = getString(R.string.loading),
        val progressColor: Int = R.color.white,
        val textSize: Int = 14,
        val progressSize: Int = 24
    )


    override fun start() {
        show()
    }

    override fun destroy() {
        dismiss()
    }

    override fun success() {
    }

    override fun failed() {
    }
}