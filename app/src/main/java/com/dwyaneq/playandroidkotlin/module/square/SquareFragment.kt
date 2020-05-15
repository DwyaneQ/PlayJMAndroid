package com.dwyaneq.playandroidkotlin.module.square

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.databinding.FragmentSquareBinding
import com.dwyaneq.playandroidkotlin.module.nav.NavigatorFragment
import com.dwyaneq.playandroidkotlin.module.qa.QAFragment
import com.dwyaneq.playandroidkotlin.module.system.SystemFragment
import kotlinx.android.synthetic.main.common_view_pager.*

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class SquareFragment : BaseFragment<BaseViewModel, FragmentSquareBinding>() {
    private val tabList: ArrayList<String> = arrayListOf("广场", "导航", "问答", "体系")
    private val fragmentList: ArrayList<Fragment> = arrayListOf()

    init {
        fragmentList.add(SquareListFragment())
        fragmentList.add(NavigatorFragment())
        fragmentList.add(QAFragment())
        fragmentList.add(SystemFragment())
    }

    override fun initView(savedInstanceState: Bundle?) {
        vp_content.init(this, fragmentList, true)
        magic_indicator.init(vp_content, stringList = tabList)
    }

    override fun getLayoutId(): Int = R.layout.fragment_square


}