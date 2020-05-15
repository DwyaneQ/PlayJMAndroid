package com.dwyaneq.playandroidkotlin.module.nav

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.NavigatorAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.NavigatorResult
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class NavigatorFragment : BaseFragment<NavigatorViewModel, ViewDataBinding>() {

    private val navigatorAdapter by lazy { NavigatorAdapter(R.layout.item_navigator) }
    private val dataList = arrayListOf<NavigatorResult>()

    override fun initView(savedInstanceState: Bundle?) {
        var layoutManger = LinearLayoutManager(activity)
        layoutManger.initialPrefetchItemCount = 6
        recycler_view.init(layoutManger, navigatorAdapter).let {
            it.setItemViewCacheSize(200)
            it.setHasFixedSize(true)
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        swipe_refresh.init {
            viewModel.getNavigatorList()
        }
    }

    override fun lazyLoadData() {
        showLoading()
        viewModel.getNavigatorList()
    }

    override fun createObserver() {
        viewModel.navigatorData.observe(this, Observer {
            dismissLoading()
            parseState(it, { resultList ->
                swipe_refresh.isRefreshing = false
                dataList.clear()
                dataList.addAll(resultList)
                navigatorAdapter.setNewInstance(dataList)
            })
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_navigator
}