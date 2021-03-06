package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SquareRepository {
    suspend fun getSquareList(pageIndex: Int):
            CommonResult<CommonPagerResult<ArrayList<ArticleResult>>> {
        return NetworkApi().service.getSquareList(pageIndex)
    }
}