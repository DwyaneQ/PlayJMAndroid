package com.dwyaneq.playandroidkotlin.repository

import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.UserInfo

/**
 * Created by DWQ on 2020/4/27.
 * E-Mail:lomapa@163.com
 * \@Inject constructor(private val retrofit: Retrofit) 可理解为创建该类实例时，将构造中的参数直接注入
 */
class LoginRepository {

    suspend fun login(username: String, password: String): CommonResult<UserInfo> {
        return NetworkApi().service.login(username, password)
    }

    suspend fun register(
        username: String,
        password: String,
        repasssword: String
    ): CommonResult<UserInfo> {
        return NetworkApi().service.register(username, password, repasssword)
    }
}