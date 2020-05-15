package com.dwyaneq.playandroidkotlin.adapter

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.jetpack_mvvm_base.navigation.NavHostFragment
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.NavigatorResult
import com.dwyaneq.playandroidkotlin.data.bean.SystemTabResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowLayoutManager
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowSpaceItemDecoration
import kotlinx.android.synthetic.main.common_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SystemAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<SystemTabResult, BaseViewHolder>(layoutRes) {
    val itemDecoration by lazy { FlowSpaceItemDecoration(UUi.dip2px(6f)) }
    override fun convert(holder: BaseViewHolder, item: SystemTabResult) {
        holder.setText(R.id.tv_name, item.name)
        var recyclerView = holder.getView<RecyclerView>(R.id.rv_navigator_item)
        var itemAdapter = SystemItemAdapter(R.layout.item_search_history)
        recyclerView.init(
            FlowLayoutManager(),
            itemAdapter
        )
        val articleList = arrayListOf<TabResult>()
        if (recyclerView.itemDecorationCount == 0)
            recyclerView.addItemDecoration(
                itemDecoration
            )
        articleList.addAll(item.children)
        itemAdapter.setNewInstance(articleList)
        itemAdapter.setOnItemClickListener { _, view, position ->
            val data = Bundle()
            data.putString("system_name", articleList[position].name)
            data.putInt("article_cid", articleList[position].id)
            Navigation.findNavController(view).navigate(R.id.action_system_to_system_detail, data)
        }
    }
}