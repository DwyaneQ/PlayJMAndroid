package com.dwyaneq.playandroidkotlin.module.wxgzh

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.databinding.FragmentWxgzhBinding
import kotlinx.android.synthetic.main.common_view_pager.*

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class WXGZHFragment : BaseFragment<WXGZHViewModel, FragmentWxgzhBinding>() {
    private val fragmentList: ArrayList<Fragment> = arrayListOf()
    private var mDataList: ArrayList<TabResult> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?) {
        vp_content.init(this, fragmentList, true)
        magic_indicator.init(vp_content, tabList = mDataList)
    }

    override fun getLayoutId(): Int = R.layout.fragment_wxgzh

    override fun lazyLoadData() {
        showLoading()
        viewModel.getWXTabList()
    }

    override fun createObserver() {
        viewModel.tabData.observe(this, Observer {
            parseState(it, { result ->
                mDataList.addAll(result)
                result.forEach { wxTabResult ->
                    fragmentList.add(WXArticleListFragment.newInstance(wxTabResult))
                }
                vp_content.offscreenPageLimit = fragmentList.size
                magic_indicator.navigator.notifyDataSetChanged()
                vp_content.adapter?.notifyDataSetChanged()
            })
            dismissLoading()
        })
    }
}