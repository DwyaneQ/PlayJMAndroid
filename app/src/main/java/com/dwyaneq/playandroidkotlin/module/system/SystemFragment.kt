package com.dwyaneq.playandroidkotlin.module.system

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.SystemAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.SystemTabResult
import com.dwyaneq.playandroidkotlin.databinding.FragmentSystemBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SystemFragment : BaseFragment<SystemViewModel, FragmentSystemBinding>() {
    private val systemAdapter by lazy { SystemAdapter(R.layout.item_navigator) }
    private val dataList = arrayListOf<SystemTabResult>()

    override fun initView(savedInstanceState: Bundle?) {
        var layoutManger = LinearLayoutManager(activity)
        layoutManger.initialPrefetchItemCount = 6
        recycler_view.init(layoutManger, systemAdapter).let {
            it.setItemViewCacheSize(200)
            it.setHasFixedSize(true)
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        swipe_refresh.init {
            viewModel.getSystemTab()
        }
    }
    override fun lazyLoadData() {
        showLoading()
        viewModel.getSystemTab()
    }

    override fun createObserver() {
        viewModel.systemTabData.observe(this, Observer {
            dismissLoading()
            parseState(it, { resultList ->
                swipe_refresh.isRefreshing = false
                dataList.clear()
                dataList.addAll(resultList)
                systemAdapter.setNewInstance(dataList)
            })
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_system
}