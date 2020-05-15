package com.dwyaneq.playandroidkotlin.common.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.SettingUtil
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 * 自定义类控件相关的拓展函数
 */

//绑定普通的Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}


fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(context.applicationContext)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}

//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //设置主题颜色
        setColorSchemeColors(SettingUtil.getColor(context.applicationContext))
    }
}

/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(context.applicationContext))
    title = titleStr
    return this
}

/**
 * 初始化有返回键的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.drawable.ic_back_arrow,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(SettingUtil.getColor(context.applicationContext))
    title = Html.fromHtml(titleStr)
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}

fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}

/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击 跳转前做登录校验
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastloginClickTime = 0L
fun View.clickNoRepeatLogin(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastloginClickTime != 0L && (currentTime - lastloginClickTime < interval)) {
            return@setOnClickListener
        }
        lastloginClickTime = currentTime
        if (CacheUtil.isLogin()) {
            action(it)
        } else {
            Navigation.findNavController(it).navigate(R.id.action_nav_main_to_login)
        }
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击 跳转前做登录校验
 * @param view 触发的view集合
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
fun clickNoRepeatLogin(vararg view: View?, interval: Long = 500, action: (view: View) -> Unit) {
    view.forEach {
        it?.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (lastloginClickTime != 0L && (currentTime - lastloginClickTime < interval)) {
                return@setOnClickListener
            }
            lastloginClickTime = currentTime
            if (CacheUtil.isLogin()) {
                action(it)
            } else {
                Navigation.findNavController(it).navigate(R.id.action_nav_main_to_login)
            }
        }
    }
}

fun MagicIndicator.init(
    viewPager2: ViewPager2,
    stringList: ArrayList<String> = arrayListOf(),
    tabList: ArrayList<TabResult> = arrayListOf()
) {
    val commonNavigator = CommonNavigator(viewPager2.context)
    commonNavigator.adapter = object : CommonNavigatorAdapter() {
        override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
            val colorTransitionPagerTitleView =
                ColorTransitionPagerTitleView(context)
            colorTransitionPagerTitleView.normalColor = Color.WHITE
            colorTransitionPagerTitleView.selectedColor = Color.WHITE
            if (stringList.isNotEmpty()) {
                colorTransitionPagerTitleView.text = stringList[index]
            } else {
                colorTransitionPagerTitleView.text =
                    Html.fromHtml(tabList[index].name)
            }
            colorTransitionPagerTitleView.setOnClickListener {
                viewPager2.currentItem = index
            }
            return colorTransitionPagerTitleView
        }

        override fun getCount(): Int = if (stringList.isEmpty()) tabList.size else stringList.size


        override fun getIndicator(context: Context?): IPagerIndicator {
            val indicator = LinePagerIndicator(context)
            indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
            indicator.setColors(Color.WHITE)
            return indicator
        }
    }
    this.navigator = commonNavigator
    this.visibility = View.VISIBLE
    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@init.onPageSelected(position)
//            action.invoke(position)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@init.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@init.onPageScrollStateChanged(state)
        }
    })
}
