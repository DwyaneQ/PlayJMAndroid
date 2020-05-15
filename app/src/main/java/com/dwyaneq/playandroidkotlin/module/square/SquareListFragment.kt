package com.dwyaneq.playandroidkotlin.module.square

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.SquareListAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.databinding.FragmentSquareListBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SquareListFragment : BaseFragment<SquareViewModel, FragmentSquareListBinding>() {
    val squareListAdapter by lazy { SquareListAdapter(R.layout.item_square_article) }

    override fun initView(savedInstanceState: Bundle?) {
        recycler_view.init(LinearLayoutManager(activity), squareListAdapter).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f), true))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        squareListAdapter.let {
            it.loadMoreModule.setOnLoadMoreListener {
                viewModel.getSquareList()
            }
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
            it.addChildClickViewIds(R.id.iv_collect)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_collect) {
                    if (CacheUtil.isLogin()) {
                        val articleItem = it.data[position]
                        if (articleItem.collect) {// 取消收藏
                            (view as ImageView).setImageResource(R.drawable.ic_article_uncollect)
                            viewModel.cancelCollect(articleItem.id)
                        } else {//  收藏
                            (view as ImageView).setImageResource(R.drawable.ic_article_collect)
                            viewModel.collect(articleItem.id)
                        }
                    } else {
                        navigationPopUpTo(view, R.id.action_nav_main_to_login)
                    }
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
            viewModel.getSquareList(true)
        }

    }

    override fun lazyLoadData() {
        showLoading()
        viewModel.getSquareList(true)
    }

    override fun createObserver() {
        viewModel.squareData.observe(this, Observer {
            dismissLoading()
            swipe_refresh.isRefreshing = false
            if (it.isSuccess) {
                when {
                    (it.isFirstEmpty) -> {
                        squareListAdapter.isUseEmpty = true
                    }
                    (it.isRefresh) -> {
                        squareListAdapter.isUseEmpty = false
                        squareListAdapter.setNewInstance(it.listData)
                    }
                    else -> {
                        squareListAdapter.addData(it.listData)
                        squareListAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
            } else {
                if (it.isRefresh) {
                    squareListAdapter.isUseEmpty = true
                } else {
                    squareListAdapter.isUseEmpty = false
                    squareListAdapter.loadMoreModule.loadMoreFail()
                }
            }

        })
        viewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                toast(it.errorMsg)
                for (index in squareListAdapter.data.indices) {
                    if (squareListAdapter.data[index].id == it.id) {
                        squareListAdapter.data[index].collect = it.collect
                        squareListAdapter.notifyItemChanged(index + 1)
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
                        for (item in squareListAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in squareListAdapter.data) {
                        item.collect = false
                    }
                }
                squareListAdapter.notifyDataSetChanged()
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            collect.observe(viewLifecycleOwner, Observer {
                for (index in squareListAdapter.data.indices) {
                    if (squareListAdapter.data[index].id == it.id) {
                        squareListAdapter.data[index].collect = it.collect
                        squareListAdapter.notifyItemChanged(index + 1)
                        break
                    }
                }
            })
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_square_list
}