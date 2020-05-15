package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult

/**
 * Created by DWQ on 2020/5/12.
 * E-Mail:lomapa@163.com
 */
class ProjectRepository {
    suspend fun getProjectTabList(): CommonResult<ArrayList<TabResult>> {
        return NetworkApi().service.getProjectTabList()
    }

    suspend fun getProjectArticleList(
        pageIndex: Int,
        id: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getProjectArticleList(pageIndex, id)
    }

    suspend fun getNewArticleList(
        pageIndex: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getNewProjectList(pageIndex)
    }
}