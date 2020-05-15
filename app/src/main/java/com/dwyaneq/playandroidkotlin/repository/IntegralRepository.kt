package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.IntegralHistoryResult

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class IntegralRepository {
    suspend fun getIntegralHistoryList(pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<IntegralHistoryResult>>> {
        return NetworkApi().service.getCoinHistoryList(pageIndex)
    }
}