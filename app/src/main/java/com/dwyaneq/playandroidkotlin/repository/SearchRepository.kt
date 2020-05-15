package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.HotKeyResult

/**
 * Created by DWQ on 2020/5/8.
 * E-Mail:lomapa@163.com
 */
class SearchRepository {

    fun getSearchHistory(): ArrayList<String> {
        return CacheUtil.getSearchHistoryData()
    }

    suspend fun getHotKeyList(): CommonResult<ArrayList<HotKeyResult>> {
        return NetworkApi().service.getHotKeyList()
    }

    suspend fun getSearchList(pageIndex: Int, key: String)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getSearchList(pageIndex, key)
    }
}