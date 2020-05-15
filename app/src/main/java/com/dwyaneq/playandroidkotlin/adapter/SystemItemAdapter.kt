package com.dwyaneq.playandroidkotlin.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.HotKeyResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import java.util.*

/**
 * Created by DWQ on 2020/5/7.
 * E-Mail:lomapa@163.com
 */
class SystemItemAdapter(layoutResId: Int) :
    BaseQuickAdapter<TabResult, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: TabResult) {
        holder.setText(R.id.tv_search_history, item.name)
        holder.setGone(R.id.iv_delete, true)
        holder.setTextColor(R.id.tv_search_history, getRandomColor())
    }

    private fun getRandomColor(): Int {
        val random = Random()
        var r = 0
        var g = 0
        var b = 0
        for (i in 0..1) {
            //       result=result*10+random.nextInt(10);
            var temp: Int = random.nextInt(16)
            r = r * 16 + temp
            temp = random.nextInt(16)
            g = g * 16 + temp
            temp = random.nextInt(16)
            b = b * 16 + temp
        }
        return Color.rgb(r, g, b)
    }
}