package com.dwyaneq.playandroidkotlin.module.nav

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.data.bean.NavigatorResult
import com.dwyaneq.playandroidkotlin.repository.NavigatorRepository

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class NavigatorViewModel : BaseViewModel() {
    val repository: NavigatorRepository by lazy { NavigatorRepository() }

    var navigatorData: MutableLiveData<ResultState<ArrayList<NavigatorResult>>> = MutableLiveData()

    fun getNavigatorList() {
        request({ repository.getNavigatorList() }, navigatorData)
    }

}