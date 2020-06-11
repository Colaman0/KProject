package com.kyle.colaman.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.base.viewmodel.BaseViewModel
import com.kyle.colaman.entity.UserInfoEntity
import com.kyle.colman.helper.kHandler
import com.kyle.colman.network.KError
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val userStream = MutableLiveData<UserInfoEntity>()
    val loadingStream = MutableLiveData<Boolean>()

    fun switchType() {
        isLogin.set(!isLogin.get()!!)
    }

    fun submitLogin() {
        apiLogin(account = account.get()!!, psw = password.get()!!)
    }

    fun submitRegister() = apiSignIn(
        account = signAccount.get()!!,
        psw = signPassword.get()!!,
        rePsw = rePassword.get()!!
    )

    private fun apiLogin(account: String, psw: String) {
        if (account.isNotBlank() && psw.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO + kHandler {
                loadingStream.postValue(false)
                tipsStream.postValue(it.kMessage)
            }) {
                loadingStream.postValue(true)
                val entity = Api.login(account = account, password = psw)
                if (entity != null) {
                    userStream.postValue(entity)
                    tipsStream.postValue("登录成功")
                }
                loadingStream.postValue(false)
                delay(1000)
            }
        } else {
            loadingStream.postValue(false)
            tipsStream.postValue("账号或密码不能为空")
        }
    }

    private fun apiSignIn(account: String, psw: String, rePsw: String) {
        if (account.isNotBlank() && psw.isNotBlank()
            && rePsw.isNotBlank()
        ) {

            if (psw == rePsw) {
                viewModelScope.launch(Dispatchers.IO + kHandler { }) {
                    val result = Api.register(account = account, password = psw, rePassword = rePsw)
                    if (result != null) {
                        this@LoginRegisterViewmodel.account.set(account)
                        this@LoginRegisterViewmodel.password.set(psw)
                        switchType()
                        tipsStream.postValue("注册成功")
                    }
                }
            } else {
                tipsStream.postValue("两次密码不一致")
            }
        } else {
            tipsStream.postValue("账号或密码不能为空")
        }
    }
}