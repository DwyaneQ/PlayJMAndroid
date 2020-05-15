package com.dwyaneq.playandroidkotlin.data.bean

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */

data class BannerResult(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)