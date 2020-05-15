package com.dwyaneq.playandroidkotlin.data.bean

import java.io.Serializable

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
data class TabResult(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable