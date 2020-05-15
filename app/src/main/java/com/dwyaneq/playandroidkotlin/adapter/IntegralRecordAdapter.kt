package com.dwyaneq.playandroidkotlin.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.IntegralHistoryResult
import kotlinx.android.synthetic.main.item_integral_record.view.*

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class IntegralRecordAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<IntegralHistoryResult, BaseViewHolder>(layoutRes) ,LoadMoreModule{
    override fun convert(holder: BaseViewHolder, item: IntegralHistoryResult) {
        holder.setText(R.id.tv_detail, item.desc)
    }
}