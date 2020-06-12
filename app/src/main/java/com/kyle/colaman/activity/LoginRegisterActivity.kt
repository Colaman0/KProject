package com.kyle.colaman.activity

import android.content.Intent
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivityLoginRegisterBinding

import com.kyle.colaman.viewmodel.LoginRegisterViewmodel
import com.kyle.colman.view.CommonDialog
import com.kyle.colman.view.KActivity


class LoginRegisterActivity :
    KActivity<ActivityLoginRegisterBinding>(R.layout.activity_login_register) {

    val viewmodel by viewModels<LoginRegisterViewmodel>()
    val loadingDialog by lazy {
        CommonDialog(this)
    }

    override fun initView() {
        mImmersionBar?.transparentBar()
        binding?.activity = this
        binding?.viewmodel = viewmodel
        binding?.icLoginPasswordPswAction?.let { binding?.edtLoginPassword?.bindPasswordVisible(it) }
        binding?.icSignPasswordAction?.let { binding?.edtRegisterPassword?.bindPasswordVisible(it) }
        binding?.icSignRepasswordAction?.let {
            binding?.edtRegisterRepassword?.bindPasswordVisible(
                it
            )
        }

        viewmodel.tipsStream.observe(this, Observer {
            ToastUtils.showShort(it)
        })
        viewmodel.userStream.observe(this, Observer {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })
        viewmodel.loadingStream.observe(this, Observer {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        })
    }

    fun submit() {
        if (viewmodel.isLogin.get() == true) {
            viewmodel.submitLogin()
        } else {
            viewmodel.submitRegister()
        }
    }
}

fun EditText.bindPasswordVisible(view: ImageView, visible: Boolean = false) {
    var visibleFlag = visible
    inputType = if (visibleFlag) {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    setSelection(text.length)
    view.setImageResource(if (visibleFlag) R.mipmap.psw_visible else R.mipmap.psw_invisible)
    view.setOnClickListener {
        visibleFlag = !visibleFlag
        inputType = if (visibleFlag) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setImageResource(if (visibleFlag) R.mipmap.psw_visible else R.mipmap.psw_invisible)
        setSelection(text.length)
    }
}
