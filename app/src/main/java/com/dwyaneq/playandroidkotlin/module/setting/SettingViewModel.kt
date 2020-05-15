package com.dwyaneq.playandroidkotlin.module.setting

import androidx.databinding.ObservableField
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.playandroidkotlin.MApplication
import com.dwyaneq.playandroidkotlin.common.utils.DataCacheUtil

/**
 * Created by DWQ on 2020/5/15.
 * E-Mail:lomapa@163.com
 */
class SettingViewModel : BaseViewModel() {
    var cacheSize: ObservableField<String> = ObservableField()

    fun getCacheSize() {
        var totalCacheSize =
            DataCacheUtil.getTotalCacheSize(MApplication.instance.applicationContext)
        cacheSize.set(totalCacheSize)
    }

    fun cleanCache() {
        DataCacheUtil.clearAllCache(MApplication.instance.applicationContext)
        cacheSize.set("0KB")
    }
}