package com.dwyaneq.playandroidkotlin.module.system

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.HomeArticleAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.databinding.FragmentSearchResultBinding
import com.dwyaneq.playandroidkotlin.databinding.FragmentSystemDetailBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/8.
 * E-Mail:lomapa@163.com
 */
class SystemDetailFragment : BaseFragment<SystemViewModel, FragmentSystemDetailBinding>() {
    var cid: Int = -1
    lateinit var systemTitle: String
    private val articleAdapter by lazy { HomeArticleAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.init(layoutManager, articleAdapter).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f), true))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        articleAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.getSystemArticleList(cid)
        }
        articleAdapter.let {
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
            it.addChildClickViewIds(R.id.iv_collect)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_collect) {
                    // 收藏
                    if (CacheUtil.isLogin()) {
                        val articleItem = articleAdapter.data[position]
                        if (articleItem.collect) {// 取消收藏
                            viewModel.cancelCollect(articleItem.id)
                            (view as ImageView).setImageResource(R.drawable.ic_article_uncollect)
                        } else {//  收藏
                            viewModel.collect(articleItem.id)
                            (view as ImageView).setImageResource(R.drawable.ic_article_collect)
                        }
                    } else {
                        navigationPopUpTo(view, R.id.action_search_result_main_to_login)
                    }
                }
            }
            it.setOnItemClickListener { _, view, position ->
                //  跳转详情
                val data = Bundle()
                data.putString("article_url", articleAdapter.data[position].link)
                data.putString("article_title", articleAdapter.data[position].title)
                navigationPopUpTo(view, R.id.action_search_result_to_webview, data, false)
            }
        }
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipe_refresh.init {
            viewModel.getSystemArticleList(cid, true)
        }
    }

    override fun initData() {
        cid = arguments?.getInt("article_cid")!!
        systemTitle = arguments?.getString("system_name").toString()
    }

    override fun getLayoutId(): Int = R.layout.fragment_search_result

    override fun getTitle(): String = systemTitle


    override fun lazyLoadData() {
        showLoading()
        viewModel.getSystemArticleList(cid, true)
    }

    override fun createObserver() {
        viewModel.articleData.observe(this, Observer {
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
                if (it.listData.size < 20)
                    articleAdapter.loadMoreModule.loadMoreEnd(true)
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
                        articleAdapter.notifyItemChanged(index)
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
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            })
        }
    }
}