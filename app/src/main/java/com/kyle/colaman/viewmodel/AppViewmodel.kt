package com.kyle.colaman.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kyle.colaman.AppContext
import com.kyle.colman.view.KApplication

/**
 * Author   : kyle
 * Date     : 2020/6/23
 * Function : app viewmodel
 */
object AppViewmodel : AndroidViewModel(KApplication.getAppContext() as Application) {

}