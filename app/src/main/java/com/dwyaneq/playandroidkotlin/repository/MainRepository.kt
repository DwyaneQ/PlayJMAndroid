package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.PersonalCoinResult

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
class MainRepository {
    suspend fun getPersonalCoin(): CommonResult<PersonalCoinResult> {
        return NetworkApi().service.getPersonalCoin()
    }

    suspend fun logout(): CommonResult<Any?> {
        return NetworkApi().service.logout()
    }
}