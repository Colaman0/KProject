package com.kyle.colaman.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.kyle.colaman.ActionFragment
import com.kyle.colaman.FragmentAdapter
import com.kyle.colaman.IActionFragment
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivityMainBinding
import com.kyle.colaman.entity.*
import com.kyle.colaman.entity.error.LoginError
import com.kyle.colaman.helper.*
import com.kyle.colaman.viewmodel.MainViewModel
import com.kyle.colman.helper.kHandler
import com.kyle.colman.helper.logd
import com.kyle.colman.network.ApiException
import com.kyle.colman.network.IExceptionFilter
import com.kyle.colman.network.KError
import com.kyle.colman.others.StateObserver
import com.kyle.colman.view.KActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : KActivity<ActivityMainBinding>(R.layout.activity_main) {
    var currenAction: NaviAction? = null
    val viewpagerAdapter by lazy {
        FragmentAdapter(this)
    }
    val viewModel: MainViewModel by viewModels()

    companion object {
        val pool = RecyclerView.RecycledViewPool()
    }

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        viewModel.livedata.observe(this, StateObserver(
            loading = {
                LogUtils.d("loading")
            },
            completed = {
                LogUtils.d("completed")
            },
            fail = {
                LogUtils.d(it)
            }, success = {
                LogUtils.d("成功")
            })
        )
    }

    override fun initView() {
        initToolbar()
        initViewPager()
        binding.bottomBar.setOnNavigationItemSelectedListener { item ->
            var action: NaviAction? = null
            when (item.itemId) {
                R.id.guangchang -> action = ActionGuangchang
                R.id.tixi -> action = ActionTixi
                R.id.wenda -> action = ActionWenda
                R.id.xiangmu -> action = ActionXiangmu
                R.id.shouye -> action = ActionMain
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            action?.run {
                switchContent(this)
            }
            true
        }
        UserUtil.isLogin()
        switchContent(ActionMain)
    }


    fun initViewPager() {
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(ActionMain, MainDataCreator())
        )
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(
                ActionGuangchang,
                GuangchangCreator()
            )
        )
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(ActionXiangmu, XiangmuCreator())
        )
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(ActionWenda, WendaCreator())
        )
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(ActionTixi, MainDataCreator())
        )

        viewpager.isUserInputEnabled = false
        viewpager.offscreenPageLimit = viewpagerAdapter.itemCount
        viewpager.adapter = viewpagerAdapter
    }


    fun actionScrollTop() {
        (viewpagerAdapter.getItem(viewpager.currentItem) as IActionFragment).scrollTop()
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
        if (UserUtil.isLogin()) {
            navigation_view.getHeaderView(0).findViewById<TextView>(R.id.user_name).text =
                UserUtil.getUserInfo()!!.nickname
        }
    }

    /**
     * 切换内容，重新请求
     */

    fun switchContent(action: NaviAction) {
        if (currenAction?.equals(action) == true) {
            return
        }
        currenAction = action
        viewpagerAdapter.arrayList.forEach {
            if (it.findAction() === action) {
                if (action == ActionTixi) {
                    toolbar.elevation = 0f
                } else {
                    toolbar.elevation = SizeUtils.dp2px(5f).toFloat()
                }
                viewpager.setCurrentItem(viewpagerAdapter.arrayList.indexOf(it), false)
                return@forEach
            }
        }
        toolbar.title = action.text
        lifecycleScope.launch {
            viewModel.reload()
        }
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)

        val drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.account,
            R.string.account
        ) {

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

object LoginFilter : IExceptionFilter {
    override fun isCreate(throwable: Throwable): Boolean {
        return throwable is ApiException && throwable.code == -1001
    }

    override fun createKError(throwable: Throwable): KError {
        return KError(throwable, errorType = LoginError)
    }

    override fun onCatch() {
        val topActivity = ActivityUtils.getTopActivity()
        CoroutineScope(Dispatchers.Main + SupervisorJob() + kHandler { }).launch {
            MaterialDialog(topActivity).show {
                cancelable(false)
                title(text = context.getString(R.string.need_login_title))
                message(text = context.getString(R.string.need_login_tips))
                positiveButton(text = context.getString(R.string.confirm)) {
                    // 结束其他activity 并且跳转到登录页面
                    val intent = Intent(topActivity, LoginRegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    ActivityUtils.getTopActivity().startActivity(intent)
                }
                negativeButton(text = "取消") {
                    dismiss()
                }
            }
        }
    }
}