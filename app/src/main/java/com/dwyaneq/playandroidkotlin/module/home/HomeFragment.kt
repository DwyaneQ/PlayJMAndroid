package com.dwyaneq.playandroidkotlin.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.HomeArticleAdapter
import com.dwyaneq.playandroidkotlin.adapter.HomeBannerAdapter
import com.dwyaneq.playandroidkotlin.aop.CheckLogin
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.BannerResult
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.databinding.FragmentHomeBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast


/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    private val bannerList: ArrayList<BannerResult> = ArrayList()
    private val bannerAdapter: HomeBannerAdapter by lazy { HomeBannerAdapter(bannerList) }

    private val articleAdapter by lazy { HomeArticleAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.init(layoutManager, articleAdapter).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f), false))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }

        articleAdapter.let {
            it.animationEnable = true
            it.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
            it.loadMoreModule.setOnLoadMoreListener {
                viewModel.getArticleList(false)
            }
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
            it.addChildClickViewIds(R.id.iv_collect)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_collect) {
                    collect(position, view)
                }
            }
            it.setOnItemClickListener { _, view, position ->
                //  跳转详情
                val data = Bundle()
                data.putString("article_url", articleAdapter.data[position].link)
                data.putString("article_title", articleAdapter.data[position].title)
                navigationPopUpTo(view, R.id.action_main_to_webview, data, false)
            }
        }
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipe_refresh.init {
            viewModel.getArticleList(true)
        }
    }

    @CheckLogin
    private fun collect(position: Int, view: View) {
        val articleItem = articleAdapter.data[position]
        if (articleItem.collect) {// 取消收藏
            (view as ImageView).setImageResource(R.drawable.ic_article_uncollect)
            viewModel.cancelCollect(articleItem.id)
        } else {//  收藏
            (view as ImageView).setImageResource(R.drawable.ic_article_collect)
            viewModel.collect(articleItem.id)
        }
    }

    private fun initHeaderView() {
        val headerView = LayoutInflater.from(context).inflate(R.layout.layout_home_banner, null)
        val banner = headerView.findViewById<Banner<*, *>>(R.id.banner)
        banner.adapter = bannerAdapter
        banner.indicator = CircleIndicator(activity)
        banner.setIndicatorNormalColor(resources.getColor(R.color.white))
        banner.setIndicatorSelectedColor(resources.getColor(R.color.colorPrimary))
        banner.start()
        banner.setOnBannerListener { _, position ->
            //  跳转详情
            val data = Bundle()
            data.putString("article_url", bannerList[position].url)
            data.putString("article_title", bannerList[position].title)
            navigationPopUpTo(banner, R.id.action_main_to_webview, data)
        }
        articleAdapter.setHeaderView(headerView)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun isBackBtnVisible(): Boolean = false

    override fun getTitleRes(): Int = R.string.btm_nav_home

    override fun lazyLoadData() {
        showLoading()
        viewModel.getBannerList()
        viewModel.getArticleList(true)
    }

    override fun createObserver() {
        viewModel.homeBannerData.observe(this, Observer { result ->
            parseState(result
                , {
                    bannerList.clear()
                    bannerList.addAll(it)
                    initHeaderView()
//                    bannerAdapter.notifyDataSetChanged()
                })
        })
        viewModel.homeArticleData.observe(this, Observer {
            dismissLoading()
            swipe_refresh.isRefreshing = false
            if (it.isSuccess) {

                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                        articleAdapter.isUseEmpty = true
                    }
                    //第一页
                    it.isRefresh -> {
                        articleAdapter.isUseEmpty = false
                        articleAdapter.setNewInstance(it.listData)
                    }
                    //不是第一页
                    else -> {
                        articleAdapter.loadMoreModule.loadMoreComplete()
                        articleAdapter.addData(it.listData)
                    }
                }

            } else {
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
                    articleAdapter.isUseEmpty = true
                } else {
                    articleAdapter.isUseEmpty = false
                    articleAdapter.loadMoreModule.loadMoreFail()
                }
            }
        })
        viewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                toast(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index + 1)
                        break
                    }
                }
            }
        })
        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userinfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in articleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in articleAdapter.data) {
                        item.collect = false
                    }
                }
                articleAdapter.notifyDataSetChanged()
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            collect.observe(viewLifecycleOwner, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index + 1)
                        break
                    }
                }
            })
        }
    }

}
