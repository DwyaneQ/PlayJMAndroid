package com.dwyaneq.playandroidkotlin.module.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.databinding.FragmentMainBinding
import com.dwyaneq.playandroidkotlin.module.home.HomeFragment
import com.dwyaneq.playandroidkotlin.module.project.ProjectFragment
import com.dwyaneq.playandroidkotlin.module.square.SquareFragment
import com.dwyaneq.playandroidkotlin.module.wxgzh.WXGZHFragment
import kotlinx.android.synthetic.main.drawer_main_menu_layout.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.ext.util.setOnclickNoRepeat
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {
    var fragments = arrayListOf<Fragment>()

    val homeFragment: HomeFragment by lazy { HomeFragment() }
    val squareFragment: SquareFragment by lazy { SquareFragment() }
    val projectFragment: ProjectFragment by lazy { ProjectFragment() }
    val wxgzhFragment: WXGZHFragment by lazy { WXGZHFragment() }

    init {
        fragments.apply {
            add(homeFragment)
            add(squareFragment)
            add(projectFragment)
            add(wxgzhFragment)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.mainViewModel = viewModel
        vp_main.init(this, fragments, false).run {
            // 设置预加载页数
            offscreenPageLimit = fragments.size
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    var titleRes = R.string.btm_nav_home
                    ll_btn.visibility = View.GONE
                    when (position) {
                        0 -> {
                            titleRes = R.string.btm_nav_home
                            ll_btn.let { it ->
                                it.visibility = View.VISIBLE
                                iv_right_btn.setImageResource(R.drawable.ic_search)
                                it.onClick {
                                    navigationPopUpTo(
                                        iv_right_btn,
                                        R.id.action_home_to_search,
                                        null,
                                        false
                                    )
                                }
                            }
                        }
                        1 -> {
                            titleRes = R.string.btm_nav_square
                        }
                        2 -> {
                            titleRes = R.string.btm_nav_project
                        }
                        3 -> {
                            titleRes = R.string.btm_nav_gzh
                        }
                    }
                    tv_main_title.setText(titleRes)
                }
            })
        }

        //初始化 bottombar
        btm_navigation.run {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_home -> vp_main.setCurrentItem(0, false)
                    R.id.menu_square -> vp_main.setCurrentItem(1, false)
                    R.id.menu_project -> vp_main.setCurrentItem(2, false)
                    R.id.menu_gzh -> vp_main.setCurrentItem(3, false)
                }
                true
            }
        }
        lly_menu.onClick {
            drawer_layout.openDrawer(Gravity.LEFT)
        }
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerOpened(drawerView: View) {
                viewModel.getPersonalCoin()
                drawerView.isClickable = true
            }

        })
        appViewModel.userinfo.value?.let {
            viewModel.username.set(if (it.nickname.isEmpty()) it.username else it.nickname)
        }
        setOnclickNoRepeat(
            lly_logout,
            rl_user,
            lly_score,
            lly_score_rule,
            lly_collection,
            lly_setting
        ) {
            if (CacheUtil.isLogin())
                when (it.id) {
                    R.id.lly_logout -> {// 退出登录
                        activity?.let { context ->
                            AlertDialog.Builder(context)
                                .setMessage(R.string.logout_tips)
                                .setTitle(R.string.logout_dialog_title)
                                .setPositiveButton(
                                    "确定"
                                ) { _, _ ->
                                    // 清空cookie缓存
                                    NetworkApi().cookieJar.clear()
                                    appViewModel.userinfo.postValue(null)
                                    CacheUtil.setUser(null)
                                    view?.let {
                                        Navigation.findNavController(it).navigateUp()
                                        drawer_layout.close()
                                    }
                                    viewModel.logout()
                                }
                                .setNeutralButton("取消", null)
                                .create()
                                .show()
                        }
                    }
                    R.id.rl_user -> {// 如果未登录，跳转登陆
                        if (!CacheUtil.isLogin()) {
                            navigationPopUpTo(it, R.id.action_nav_main_to_login)
                        }
                    }
                    R.id.lly_score -> {// 我的积分
                        navigationPopUpTo(it, R.id.action_main_to_integral)
                    }
                    R.id.lly_score_rule -> {// 积分规则
                        val data = Bundle()
                        data.putString("article_url", "https://www.wanandroid.com/blog/show/2653")
                        data.putString("article_title", "积分规则")
                        navigationPopUpTo(it, R.id.action_main_to_webview, data)
                    }
                    R.id.lly_collection -> {// 我的收藏
                        navigationPopUpTo(it, R.id.action_main_to_collection)
                    }
                    R.id.lly_setting -> {
                        navigationPopUpTo(it, R.id.action_main_to_setting)
                    }
                }
            else {
                navigationPopUpTo(view!!, R.id.action_nav_main_to_login)
                drawer_layout.close()
            }

        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun lazyLoadData() {
        viewModel.getPersonalCoin()
        appViewModel.userinfo.value?.run {

        }
    }

    override fun createObserver() {
        appViewModel.run {
            userinfo.observe(viewLifecycleOwner, Observer {
                it.notNull({
                    // 登录成功请求数据
                    viewModel.getPersonalCoin()
                    lly_logout.visibility = View.VISIBLE
                    viewModel.username.set(if (it.nickname.isEmpty()) it.username else it.nickname)
                }, {
                    viewModel.username.set("请登录")
                    viewModel.rank.set("排名：-")
                    viewModel.level.set("等级：-")
                    lly_logout.visibility = View.GONE
                })
                drawer_layout.close()
            })
        }
        viewModel.personalCoinData.observe(this, Observer {
            parseState(it, { result ->
                viewModel.rank.set("排名：${result.rank}")
                viewModel.level.set("等级：Lv${result.level}")
            })
        })
    }
}