package com.dwyaneq.playandroidkotlin.module.main

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.data.bean.PersonalCoinResult
import com.dwyaneq.playandroidkotlin.repository.MainRepository
import me.hgj.jetpackmvvm.databind.StringObservableField

/**
 * Created by DWQ on 2020/5/9.
 * E-Mail:lomapa@163.com
 */
class MainViewModel : BaseViewModel() {
    val mainRepository by lazy { MainRepository() }

    var username = StringObservableField("请登录")

    var level = StringObservableField("等级：-")

    var rank = StringObservableField("排名：-")

    var personalCoinData: MutableLiveData<ResultState<PersonalCoinResult>> =
        MutableLiveData()

    fun getPersonalCoin() {
        request({
            mainRepository.getPersonalCoin()
        }, personalCoinData)
    }
    var logoutData: MutableLiveData<ResultState<Any?>> = MutableLiveData()
    fun logout() {
        request({ mainRepository.logout() }, logoutData)
    }
}