package com.dwyaneq.playandroidkotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R

/**
 * Created by DWQ on 2020/5/7.
 * E-Mail:lomapa@163.com
 */
class SearchHistoryAdapter(layoutResId: Int) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_search_history, item)
    }

}