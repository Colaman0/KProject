package com.colaman.wanandroid

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.common.rx.binLife
import com.colaman.kyle.common.rx.bindStatusImpl
import com.colaman.kyle.common.rx.fullSubscribe
import com.colaman.kyle.view.CommonDialog
import com.colaman.wanandroid.databinding.ActivityLoginRegisterBinding
import com.colaman.wanandroid.ex.bindPasswordVisible
import com.colaman.wanandroid.viewmodel.LoginRegisterViewmodel


class LoginRegisterActivity : CommonActivity<ActivityLoginRegisterBinding, LoginRegisterViewmodel>() {
    var currentAction = 0

    override fun initLayoutRes() = R.layout.activity_login_register

    override fun initView() {
        mImmersionBar?.transparentBar()

        binding.edtLoginPassword.bindPasswordVisible(binding.icLoginPasswordPswAction)
        binding.edtRegisterPassword.bindPasswordVisible(binding.icSignPasswordAction)
        binding.edtRegisterRepassword.bindPasswordVisible(binding.icSignRepasswordAction)

        viewModel?.tipsStream?.observe(this, Observer {
            ToastUtils.showShort(it)
        })
    }

    override fun createViewModel() = LoginRegisterViewmodel()


    fun submit() {
        if (viewModel?.isLogin?.get() == true) {
            viewModel!!.submitLogin()
                    .binLife(this)
                    .bindStatusImpl(CommonDialog(this))
                    .doOnError {
                        ToastUtils.showShort(it.message)
                    }
                    .doOnComplete {
                        goToActivity(MainActivity::class.java)
                    }
                    .fullSubscribe()
        } else {
            viewModel!!.submitRegister()
                    .binLife(this)
                    .bindStatusImpl(CommonDialog(this))
                    .doOnError {
                        ToastUtils.showShort(it.message)
                    }
                    .fullSubscribe()
        }
    }
}
