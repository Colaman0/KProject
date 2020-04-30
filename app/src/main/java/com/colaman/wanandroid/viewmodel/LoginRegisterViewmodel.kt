package com.colaman.wanandroid.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colaman.kyle.base.viewmodel.BaseViewModel
import com.colaman.wanandroid.api.Api
import com.colaman.wanandroid.entity.UserInfoEntity
import io.reactivex.Observable

/**
 * Author   : kyle
 * Date     : 2020/4/27
 * Function : 登录注册viewmodel
 */
class LoginRegisterViewmodel : BaseViewModel() {
    val account = ObservableField<String>("")
    val signAccount = ObservableField<String>("")
    val signPassword = ObservableField<String>("")
    val password = ObservableField<String>("")
    val rePassword = ObservableField<String>("")
    val isLogin = ObservableField(true)
    val tipsStream = MutableLiveData<String>()

    fun switchType() {
        isLogin.set(!isLogin.get()!!)
    }


    fun submitLogin() = apiLogin(account = account.get()!!, psw = password.get()!!)

    fun submitRegister() = apiSignIn(account = signAccount.get()!!, psw = signPassword.get()!!, rePsw = rePassword.get()!!)

    private fun apiLogin(account: String, psw: String): Observable<UserInfoEntity> {
        return if (account.isNotBlank() && psw.isNotBlank()) {
            Api.login(account = account, password = psw)
                    .doOnNext {
                        tipsStream.postValue("登录成功")
                    }
        } else {
            tipsStream.postValue("账号或密码不能为空")
            Observable.empty()
        }
    }

    private fun apiSignIn(account: String, psw: String, rePsw: String): Observable<UserInfoEntity> {
        if (account.isNotBlank() && psw.isNotBlank()
                && rePsw.isNotBlank()) {
            if (psw == rePsw) {
                return Api.register(account = account, password = psw, rePassword = rePsw)
                        .doOnNext {
                            this.account.set(account)
                            this.password.set(psw)
                            switchType()
                            tipsStream.postValue("注册成功")
                        }
            } else {
                tipsStream.postValue("两次密码不一致")
            }
        } else {
            tipsStream.postValue("账号或密码不能为空")
        }
        return Observable.empty()
    }
}