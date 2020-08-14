package com.dwyaneq.playandroidkotlin.module.project

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.ProjectArticleAdapter
import com.dwyaneq.playandroidkotlin.aop.CheckLogin
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.databinding.FragmentProjectArticleListBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/12.
 * E-Mail:lomapa@163.com
 */
class ProjectArticleListFragment :
    BaseFragment<ProjectViewModel, FragmentProjectArticleListBinding>() {
    lateinit var projectTab: TabResult

    private val projectArticleAdapter by lazy { ProjectArticleAdapter(R.layout.item_project_article) }

    override fun initView(savedInstanceState: Bundle?) {
        projectTab = arguments?.getSerializable("tab_result") as TabResult
        recycler_view.init(
            LinearLayoutManager(activity)
            , projectArticleAdapter
        ).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(
                LinearSpaceItemDecoration(0, UUi.dip2px(8f), true)
            )
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        projectArticleAdapter.let {
            it.loadMoreModule.setOnLoadMoreListener {
                viewModel.getProjectArticleList(projectTab.id)
            }
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
            it.addChildClickViewIds(R.id.iv_collect)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_collect) {
                    collect(position,view)
                }
            }
            it.setOnItemClickListener { _, view, position ->
                //  跳转详情
                val data = Bundle()
                data.putString("article_url", it.data[position].link)
                data.putString("article_title", it.data[position].title)
                navigationPopUpTo(view, R.id.action_main_to_webview, data, false)
            }
        }
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipe_refresh.init {
            viewModel.getProjectArticleList(projectTab.id, true)
        }
    }

    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_project_article_list

    override fun lazyLoadData() {
        showLoading()
        viewModel.getProjectArticleList(projectTab.id, true)
    }

    override fun createObserver() {
        viewModel.projectArticleData.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            dismissLoading()
            if (it.isSuccess) {
                when {
                    (it.isFirstEmpty) -> {
                        projectArticleAdapter.isUseEmpty = true
                        projectArticleAdapter.isUseEmpty
                    }
                    (it.isRefresh) -> {
                        projectArticleAdapter.isUseEmpty = false
                        projectArticleAdapter.setNewInstance(it.listData)
                    }
                    else -> {
                        projectArticleAdapter.addData(it.listData)
                        projectArticleAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
            } else {
                if (it.isRefresh) {
                    projectArticleAdapter.isUseEmpty = true
                } else {
                    projectArticleAdapter.isUseEmpty = false
                    projectArticleAdapter.loadMoreModule.loadMoreFail()
                }
            }

        })
        viewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                toast(it.errorMsg)
                for (index in projectArticleAdapter.data.indices) {
                    if (projectArticleAdapter.data[index].id == it.id) {
                        projectArticleAdapter.data[index].collect = it.collect
                        projectArticleAdapter.notifyItemChanged(index + 1)
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
                        for (item in projectArticleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in projectArticleAdapter.data) {
                        item.collect = false
                    }
                }
                projectArticleAdapter.notifyDataSetChanged()
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            collect.observe(viewLifecycleOwner, Observer {
                for (index in projectArticleAdapter.data.indices) {
                    if (projectArticleAdapter.data[index].id == it.id) {
                        projectArticleAdapter.data[index].collect = it.collect
                        projectArticleAdapter.notifyItemChanged(index + 1)
                        break
                    }
                }
            })
        }
    }

    companion object {
        fun newInstance(projectTab: TabResult): ProjectArticleListFragment {
            var data = Bundle()
            data.putSerializable("tab_result", projectTab)
            val fragment = ProjectArticleListFragment()
            fragment.arguments = data
            return fragment
        }
    }

    @CheckLogin
    private fun collect(position: Int, view: View) {
        val articleItem = projectArticleAdapter.data[position]
        if (articleItem.collect) {// 取消收藏
            (view as ImageView).setImageResource(R.drawable.ic_article_uncollect)
            viewModel.cancelCollect(articleItem.id)
        } else {//  收藏
            (view as ImageView).setImageResource(R.drawable.ic_article_collect)
            viewModel.collect(articleItem.id)
        }
    }
}