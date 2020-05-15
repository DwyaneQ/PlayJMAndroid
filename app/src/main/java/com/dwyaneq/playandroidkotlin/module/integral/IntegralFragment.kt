package com.dwyaneq.playandroidkotlin.module.integral

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.IntegralRecordAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.ext.initFloatBtn
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.data.bean.IntegralHistoryResult
import com.dwyaneq.playandroidkotlin.databinding.FragmentIntegralBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.LinearSpaceItemDecoration
import kotlinx.android.synthetic.main.common_list.*
import kotlinx.android.synthetic.main.common_recycler_list.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class IntegralFragment : BaseFragment<IntegralViewModel, FragmentIntegralBinding>() {
    private val integralAdapter by lazy { IntegralRecordAdapter(R.layout.item_integral_record) }

    override fun initView(savedInstanceState: Bundle?) {
        recycler_view.init(LinearLayoutManager(activity), integralAdapter).let {
            // 解决notifyItem闪烁问题
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(LinearSpaceItemDecoration(0, UUi.dip2px(0.5f), true))
            //初始化FloatingActionButton
            it.initFloatBtn(floating_btn)
        }
        integralAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.getIntegralHistory()
        }
        integralAdapter.let {
            it.setEmptyView(R.layout.common_no_data)
            it.isUseEmpty = false
        }
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipe_refresh.init {
            viewModel.getIntegralHistory(true)
        }
    }

    override fun lazyLoadData() {
        showLoading()
        viewModel.getIntegralHistory(true)
    }

    override fun createObserver() {
        viewModel.integralData.observe(this, Observer {
            dismissLoading()
            swipe_refresh.isRefreshing = false
            if (it.isSuccess) {

                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                        integralAdapter.isUseEmpty = true
                    }
                    //第一页
                    it.isRefresh -> {
                        integralAdapter.isUseEmpty = false
                        integralAdapter.setNewInstance(it.listData)
                    }
                    //不是第一页
                    else -> {
                        integralAdapter.loadMoreModule.loadMoreComplete()
                        integralAdapter.addData(it.listData)
                    }
                }
                if (it.listData.size < 20)
                    integralAdapter.loadMoreModule.loadMoreEnd(true)
            } else {
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
                    integralAdapter.isUseEmpty = true
                } else {
                    integralAdapter.isUseEmpty = false
                    integralAdapter.loadMoreModule.loadMoreFail()
                }
            }
        })

    }

    override fun getLayoutId(): Int = R.layout.fragment_integral
}