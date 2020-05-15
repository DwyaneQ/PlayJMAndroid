package com.dwyaneq.jetpack_mvvm_base

import androidx.lifecycle.ViewModel
import com.dwyaneq.jetpack_mvvm_base.state.SingleLiveEvent

/**
 *  Created by DWQ on 2020/4/24.
 *  E-Mail:lomapa@163.com
 *  ViewModel基类，增加针对ProgressDialog控制方法
 */
open class BaseViewModel : ViewModel() {

    val uiChange: UiChange by lazy { UiChange() }

    /**
     * 可通知Activity/fragment 做相关Ui操作
     */
    inner class UiChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<String>() }

        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

}