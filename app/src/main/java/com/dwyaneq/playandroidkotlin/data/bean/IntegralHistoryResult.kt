package com.dwyaneq.playandroidkotlin.data.bean

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
data class IntegralHistoryResult(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)