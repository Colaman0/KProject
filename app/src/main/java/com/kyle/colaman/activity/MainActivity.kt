package com.kyle.colaman.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.kyle.colaman.FragmentAdapter
import com.kyle.colaman.R
import com.kyle.colaman.databinding.ActivityMainBinding
import com.kyle.colaman.entity.*
import com.kyle.colaman.entity.error.LoginError
import com.kyle.colaman.fragment.ActionFragment
import com.kyle.colaman.fragment.IActionFragment
import com.kyle.colaman.fragment.TixiFragment
import com.kyle.colaman.helper.*
import com.kyle.colaman.source.CollectSource
import com.kyle.colaman.viewmodel.AppViewmodel
import com.kyle.colaman.viewmodel.ItemCollectViewmodel
import com.kyle.colaman.viewmodel.MainViewModel
import com.kyle.colman.helper.bindLifeCycle
import com.kyle.colman.helper.fullSub
import com.kyle.colman.helper.kHandler
import com.kyle.colman.network.ApiException
import com.kyle.colman.network.IExceptionFilter
import com.kyle.colman.network.KError
import com.kyle.colman.recyclerview.PagingItemView
import com.kyle.colman.view.KActivity
import com.kyle.colman.view.buildIntent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : KActivity<ActivityMainBinding>(R.layout.activity_main) {
    var currenAction: NaviAction? = null
    val viewpagerAdapter by lazy {
        FragmentAdapter(this)
    }
    val viewModel: MainViewModel by viewModels()

    companion object {
        val pool = RecyclerView.RecycledViewPool()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.activity = this
    }

    override fun initView() {


        navigation_view.setNavigationItemSelectedListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.collect -> gotoCollect()
                R.id.pocket -> gotoPocket()
                R.id.login_action -> {
                    if (UserUtil.isLogin()) {
                        gotoLogout()
                    } else {
                        gotoLogin()
                    }
                }
            }
            true
        }
    }

    init {
        lifecycleScope.launchWhenStarted {
            initToolbar()
            initViewPager()
            binding?.bottomBar?.setOnNavigationItemSelectedListener { item ->
                var action: NaviAction? = null
                when (item.itemId) {
                    R.id.guangchang -> action = ActionGuangchang
                    R.id.tixi -> action = ActionTixi
                    R.id.wenda -> action = ActionWenda
                    R.id.xiangmu -> action = ActionXiangmu
                    R.id.shouye -> action = ActionMain
                }
                binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                action?.run {
                    switchContent(this)
                }
                true
            }
            switchContent(ActionMain)
        }

    }


    fun initViewPager() {
        initUser()
        initUIMode()
        viewpagerAdapter.addFragment(
            ActionFragment.newInstance(ActionMain, MainSource(viewModel))
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
            TixiFragment.newInstance()
        )
        viewpager.isUserInputEnabled = false
        viewpager.offscreenPageLimit = viewpagerAdapter.itemCount
        viewpager.adapter = viewpagerAdapter
    }


    fun actionScrollTop() {
        (viewpagerAdapter.getItem(viewpager.currentItem) as IActionFragment).scrollTop()
    }

    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    fun initUser() {
        setAccountUIShow(UserUtil.isLogin())
        navigation_view.menu.forEach {
            if (it.itemId == R.id.login_action) {
                if (UserUtil.isLogin()) {
                    navigation_view.getHeaderView(0).visibility = View.VISIBLE
                    it.title = "注销"
                    navigation_view.getHeaderView(0).findViewById<TextView>(R.id.user_name).text =
                        UserUtil.getUserInfo()!!.nickname
                } else {
                    navigation_view.getHeaderView(0).visibility = View.GONE
                    it.title = "登录"
                }
            }
        }
    }


    fun initUIMode() {
        navigation_view.menu.forEach {
            if (it.itemId == R.id.ui_mode) {
                it.actionView.findViewById<Switch>(R.id.ui_switch).apply {
                    isChecked = (SPUtils.getInstance().getBoolean("night", false))
                    text="夜间模式"
                    setOnCheckedChangeListener{view,check->
                        SPUtils.getInstance().put("night", check)
                        if (check) {
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        }else{
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                        }
                    }
                }

            }
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.search -> gotoSearch()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    /**
     * 跳转到搜索页面
     *
     */
    fun gotoSearch() {
        startActivity(buildIntent(this, SearchActivity::class.java))
    }

    fun gotoCollect() {
        val intent = buildIntent(this, ListActivity::class.java)
        intent.putExtra(Constants.data, ListActivityConfig<CollectEntity>(
            title = "收藏", source = { CollectSource() }
        ) { data, adapter, activity ->
            val item = ItemCollectViewmodel(
                data, activity
            ).apply {
                unCollectCallback = {
                    activity.remove(mutableListOf(it))
                }
            }
            item
        })
        startActivity(intent)
    }

    private fun gotoPocket() {
        startActivity(buildIntent(this, PocketActivity::class.java))
    }

    fun gotoLogin() {
        startActivity(buildIntent(this, LoginRegisterActivity::class.java))
    }

    fun gotoLogout() {
        MaterialDialog(this).show {
            cancelable(false)
            title(text = "退出登录")
            message(text = "退出后本地信息将被清除")
            negativeButton(text = "取消") {
                dismiss()
            }
            positiveButton(text = "确认") {
                AppViewmodel.viewModelScope.launch {
                    UserUtil.clearCache(context)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.d("onnewIntent")
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