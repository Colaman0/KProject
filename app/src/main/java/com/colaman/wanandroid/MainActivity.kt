package com.colaman.wanandroid

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.base.BaseActivity
import com.colaman.wanandroid.databinding.ActivityMainBinding
import com.colaman.wanandroid.entity.*
import com.colaman.wanandroid.viewmodel.MainViewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun createViewModel() = MainViewModel()

    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {
        binding.bottomBar.setOnMenuItemClickListener { item ->
            true
        }

        binding.bottomBar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            var action: NaviAction? = null
            when (item.itemId) {
                R.id.guangchang -> action = ActionGuangchang
                R.id.daohang -> action = ActionDaohang
                R.id.tixi -> action = ActionTixi
                R.id.wenda -> action = ActionWenda
                R.id.xiangmu -> action = ActionXiangmu
                R.id.gongzhonghao -> action = ActionGongzhonghao
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            action?.run {
                switchContent(this)
            }
            true
        }

        binding.floatButton.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#3d84a8"));
            setOnClickListener {

            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }


    }

    /**
     * 切换内容，重新请求
     */
    fun switchContent(action: NaviAction) {
        ToastUtils.showShort(action.text)
    }
}
