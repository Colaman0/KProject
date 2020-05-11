package com.colaman.wanandroid

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.blankj.utilcode.util.ToastUtils
import com.colaman.kyle.base.BaseActivity
import com.colaman.wanandroid.databinding.ActivityMainBinding
import com.colaman.wanandroid.entity.*
import com.colaman.wanandroid.util.UserUtil
import com.colaman.wanandroid.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun createViewModel() = MainViewModel()

    override fun initLayoutRes() = R.layout.activity_main

    override fun initView() {
        initToolbar()

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
        navigation_view.post {
            navigation_view.setCheckedItem(R.id.guangchang)
            navigation_view.menu.performIdentifierAction(R.id.guangchang, 0);
        }

        UserUtil.isLogin()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun initUser() {
        setAccountUIShow(UserUtil.isLogin())
        if (UserUtil.isLogin() ) {
            navigation_view.getHeaderView(0).findViewById<TextView>(R.id.user_name).text = UserUtil.getUserInfo()!!.nickname
        }
    }

    /**
     * 切换内容，重新请求
     */
    fun switchContent(action: NaviAction) {
        toolbar.title = action.text
        ToastUtils.showShort(action.text)
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)

        val drawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.account, R.string.account) {

        }
        //通过下面这句实现toolbar和Drawer的联动：如果没有这行代码，箭头是不会随着侧滑菜单的开关而变换的（或者没有箭头），

        drawerToggle.syncState()
        //toolbar设置的图标控制drawerlayout的侧滑
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        })

        drawer_layout.addDrawerListener(drawerToggle)
    }

    /**
     * 设置导航栏中账户相关的UI 显示/隐藏
     *
     * @param show
     */
    fun setAccountUIShow(show: Boolean) {
        navigation_view.menu.setGroupVisible(R.id.group_account, false)
    }
}
