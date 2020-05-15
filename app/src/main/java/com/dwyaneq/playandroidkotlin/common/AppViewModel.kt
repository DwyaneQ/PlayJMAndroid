package com.dwyaneq.playandroidkotlin.common

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.livedata.UnPeekLiveData
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.SettingUtil
import com.dwyaneq.playandroidkotlin.data.bean.CollectBus
import com.dwyaneq.playandroidkotlin.data.bean.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息，事件通知等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userinfo = UnPeekLiveData<UserInfo>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    //全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息
    var collect = MutableLiveData<CollectBus>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}