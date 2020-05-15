package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
class WXGZHRepository {

    suspend fun getWXTabList(): CommonResult<ArrayList<TabResult>> {
        return NetworkApi().service.getWXTabList()
    }

    suspend fun getWXArticleList(
        id: Int,
        pageIndex: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getWXArticleList(id, pageIndex)
    }
}