package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.SystemTabResult

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class SystemRepository {
    suspend fun getSystemTabList(): CommonResult<ArrayList<SystemTabResult>> {
        return NetworkApi().service.getSystemTabList()
    }

    suspend fun getSystemArticleList(
        pageIndex: Int,
        cid: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getSystemArticleList(pageIndex, cid)
    }
}