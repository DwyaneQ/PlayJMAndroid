package com.dwyaneq.playandroidkotlin.module.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.databinding.FragmentProjectBinding
import kotlinx.android.synthetic.main.common_view_pager.*

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentProjectBinding>() {
    private val fragmentList: ArrayList<Fragment> = arrayListOf()
    private var dataList: ArrayList<TabResult> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?) {
        vp_content.init(this, fragmentList, true)
        magic_indicator.init(vp_content, tabList = dataList)
    }

    override fun getLayoutId(): Int = R.layout.fragment_project

    override fun lazyLoadData() {
        showLoading()
        viewModel.getProjectTabList()
    }

    override fun createObserver() {
        viewModel.projectTabData.observe(this, Observer {
            parseState(it, { tabResultList ->
                dataList.add(
                    TabResult(
                        arrayListOf(),
                        courseId = -1,
                        id = -1,
                        name = "最新项目",
                        order = -1,
                        parentChapterId = -1,
                        userControlSetTop = false,
                        visible = 0
                    )
                )
                dataList.addAll(tabResultList)
                dataList.forEach { tabResult ->
                    fragmentList.add(ProjectArticleListFragment.newInstance(tabResult))
                }
                vp_content.offscreenPageLimit = fragmentList.size
                magic_indicator.navigator.notifyDataSetChanged()
                vp_content.adapter?.notifyDataSetChanged()
            })
            dismissLoading()
        })
    }
}