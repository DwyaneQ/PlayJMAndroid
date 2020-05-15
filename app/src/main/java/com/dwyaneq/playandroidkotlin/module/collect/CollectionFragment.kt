package com.dwyaneq.playandroidkotlin.module.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.CollectionAdapter
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.databinding.FragmentIntegralBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class CollectionFragment : BaseFragment<CollectViewModel, FragmentIntegralBinding>() {
    private val collectionAdapter by lazy { CollectionAdapter(R.layout.item_collection) }
    override fun initView(savedInstanceState: Bundle?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.init(layoutManager, collectionAdapter).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }

        collectionAdapter.let {
            it.loadMoreModule.setOnLoadMoreListener {
                viewModel.getCollectionList(false)
            }
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
            it.addChildClickViewIds(R.id.iv_collect)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_collect) {// 取消收藏

                    viewModel.cancelCollection(
                        collectionAdapter.data[position].id,
                        collectionAdapter.data[position].originId
                    )
                }
            }
            it.setOnItemClickListener { _, view, position ->
                //  跳转详情
                val data = Bundle()
                data.putString("article_url", collectionAdapter.data[position].link)
                data.putString("article_title", collectionAdapter.data[position].title)
                navigationPopUpTo(view, R.id.action_collection_to_webview, data, false)
            }
        }
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipe_refresh.init {
            viewModel.getCollectionList(true)
        }
    }

    override fun lazyLoadData() {
        showLoading()
        viewModel.getCollectionList(true)
    }

    override fun createObserver() {
        viewModel.collectionData.observe(this, Observer {
            dismissLoading()
            swipe_refresh.isRefreshing = false
            if (it.isSuccess) {

                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                        collectionAdapter.isUseEmpty = true
                    }
                    //第一页
                    it.isRefresh -> {
                        collectionAdapter.isUseEmpty = false
                        collectionAdapter.setNewInstance(it.listData)
                    }
                    //不是第一页
                    else -> {
                        collectionAdapter.loadMoreModule.loadMoreComplete()
                        collectionAdapter.addData(it.listData)
                    }
                }
                if (it.listData.size < 20)
                    collectionAdapter.loadMoreModule.loadMoreEnd(true)
            } else {
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
                    collectionAdapter.isUseEmpty = true
                } else {
                    collectionAdapter.isUseEmpty = false
                    collectionAdapter.loadMoreModule.loadMoreFail()
                }
            }
        })
        viewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                toast(it.errorMsg)
                for (index in collectionAdapter.data.indices) {
                    if (collectionAdapter.data[index].id == it.id) {
                        collectionAdapter.remove(index)
                        break
                    }
                }
            }
        })
        appViewModel.collect.observe(viewLifecycleOwner, Observer {
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            for (index in collectionAdapter.data.indices) {
                if (collectionAdapter.data[index].id == it.id) {
                    collectionAdapter.remove(index)
                    break
                }
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection
}