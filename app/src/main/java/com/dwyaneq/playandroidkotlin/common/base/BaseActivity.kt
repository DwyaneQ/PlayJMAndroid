package com.dwyaneq.playandroidkotlin.common.base

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.BaseVmDbActivity
import com.dwyaneq.playandroidkotlin.R
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
abstract class BaseActivity : BaseVmDbActivity<BaseViewModel, ViewDataBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        val mCommonToolbar = findViewById<Toolbar>(R.id.common_toolbar)
        val immersionBar = ImmersionBar.with(this)
            .transparentStatusBar()
        if (mCommonToolbar != null) {
            immersionBar
                .titleBar(mCommonToolbar, false)
        }
        immersionBar.init()
    }
}