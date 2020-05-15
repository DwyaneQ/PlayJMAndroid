package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.CollectionResult

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
class CollectRepository {

    suspend fun collect(id: Int): CommonResult<Any?> {
        return NetworkApi().service.collect(id)
    }

    suspend fun cancelCollect(id: Int): CommonResult<Any?> {
        return NetworkApi().service.cancelCollect(id)
    }

    suspend fun getCollectionList(pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<CollectionResult>>> {
        return NetworkApi().service.getCollectionList(pageIndex)
    }

    suspend fun cancelCollection(id: Int, originId: Int): CommonResult<Any?> {
        return NetworkApi().service.cancelCollection(id, originId)
    }
}