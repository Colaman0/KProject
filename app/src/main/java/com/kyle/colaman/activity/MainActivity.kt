package com.kyle.colaman.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.colaman.wanandroid.api.Api
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivityMainBinding
import com.kyle.colaman.entity.ArticleEntity
import com.kyle.colaman.entity.PageDTO
import com.kyle.colaman.entity.TixiEntity
import com.kyle.colaman.entity.error.LoginError
import com.kyle.colaman.viewmodel.ItemArticleViewModel
import com.kyle.colman.helper.FilterTime
import com.kyle.colman.helper.kHandler
import com.kyle.colman.helper.toData
import com.kyle.colman.impl.IPageDTO
import com.kyle.colman.impl.IRVDataCreator
import com.kyle.colman.network.ApiException
import com.kyle.colman.network.ERROR
import com.kyle.colman.network.IExceptionFilter
import com.kyle.colman.network.KError
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.recyclerview.RecyclerItemView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : KActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        refresh_recyclerview.setDataCreator(object : IRVDataCreator<ArticleEntity> {
            override fun loadDataByPage(page: Int): LiveData<IPageDTO<ArticleEntity>> {
                return liveData<IPageDTO<ArticleEntity>>(Dispatchers.IO + kHandler {
                    ToastUtils.showLong("加载失败!")
                }) {
                    emit(Api.getHomeArticles(page).toData()!!)
                }
            }

            override fun dataToItemView(data: ArticleEntity): RecyclerItemView<*, *> {
                return ItemArticleViewModel(data)
            }

        })
    }

    @FilterTime(2000)
    fun click() {
        LogUtils.d("点击")
    }

}

object LoginFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is ApiException && throwable.code == 1001
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, errorType = LoginError)
    }
}