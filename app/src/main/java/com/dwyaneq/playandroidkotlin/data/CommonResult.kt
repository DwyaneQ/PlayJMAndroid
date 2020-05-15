package com.dwyaneq.playandroidkotlin.data

import me.hgj.jetpackmvvm.network.BaseResponse

/**
 * Created by DWQ on 2020/4/28.
 * E-Mail:lomapa@163.com
 */
class CommonResult<T>(var errorCode: Int = -1, var errorMsg: String, var data: T) : BaseResponse<T>() {
    override fun isSuccess(): Boolean = errorCode == 0

    override fun getResponseData() = data

    override fun getResponseCode() = errorCode

    override fun getResponseMsg() = errorMsg
}