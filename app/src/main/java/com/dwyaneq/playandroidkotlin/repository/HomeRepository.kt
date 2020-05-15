package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.BannerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class HomeRepository {

    suspend fun getBannerList(): CommonResult<ArrayList<BannerResult>> {
        return NetworkApi().service.getBannerList()
    }

    suspend fun getHomeArticleList(pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return withContext(Dispatchers.IO) {
            val data = async { NetworkApi().service.getArticleList(pageIndex) }
            val topData = async { getHomeTopArticleList() }
            data.await().data.datas.addAll(0, topData.await().data)
            data.await()
        }
//        return NetworkApi().service.getArticleList(pageIndex)
    }

    suspend fun getHomeTopArticleList()
            : CommonResult<ArrayList<ArticleResult>> {
        return NetworkApi().service.getTopArticleList()
    }
}