package com.dwyaneq.playandroidkotlin

import androidx.multidex.MultiDex
import com.dwyaneq.jetpack_mvvm_base.BaseApp
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

/**
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 */
class MApplication : BaseApp() {

    // 获取application实例
    companion object {
        var instance: MApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // MultiDex
        MultiDex.install(this)
//        AppInjector.init(this)
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
    }
}