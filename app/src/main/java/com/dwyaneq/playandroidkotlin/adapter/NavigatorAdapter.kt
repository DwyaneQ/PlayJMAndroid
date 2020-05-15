package com.dwyaneq.playandroidkotlin.adapter

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.NavigatorResult
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowLayoutManager
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowSpaceItemDecoration

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class NavigatorAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<NavigatorResult, BaseViewHolder>(layoutRes) {
    val itemDecoration by lazy { FlowSpaceItemDecoration(UUi.dip2px(6f)) }
    override fun convert(holder: BaseViewHolder, item: NavigatorResult) {
        holder.setText(R.id.tv_name, item.name)
        var recyclerView = holder.getView<RecyclerView>(R.id.rv_navigator_item)
        var itemAdapter = NavigatorItemAdapter(R.layout.item_search_history)
        recyclerView.init(
            FlowLayoutManager(),
            itemAdapter
        )
        val articleList = arrayListOf<ArticleResult>()
        if (recyclerView.itemDecorationCount == 0)
            recyclerView.addItemDecoration(
                itemDecoration
            )
        recyclerView.isNestedScrollingEnabled = false
        articleList.addAll(item.articles)
        itemAdapter.setNewInstance(articleList)
        itemAdapter.setOnItemClickListener { _, view, position ->
            val data = Bundle()
            data.putString("article_url", articleList[position].link)
            data.putString("article_title", articleList[position].title)
            Navigation.findNavController(view).navigate(R.id.action_main_to_webview, data)
        }
    }
}