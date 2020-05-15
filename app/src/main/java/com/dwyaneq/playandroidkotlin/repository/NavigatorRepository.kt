package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.NavigatorResult

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class NavigatorRepository {
    suspend fun getNavigatorList(): CommonResult<ArrayList<NavigatorResult>> {
        return NetworkApi().service.getNavigatorList()
    }
}