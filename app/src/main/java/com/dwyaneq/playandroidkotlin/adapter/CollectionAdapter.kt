package com.dwyaneq.playandroidkotlin.adapter

import android.os.Build
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.CollectionResult
import com.dwyaneq.playandroidkotlin.data.bean.WXArticleResult

/**
 * Created by DWQ on 2020/5/12.
 * E-Mail:lomapa@163.com
 */
class CollectionAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<CollectionResult, BaseViewHolder>(layoutRes), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: CollectionResult) {
        // 标题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY))
        } else {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title))
        }
        // 作者 分类
        var authorClassify = ""
        if (item.author.isNotEmpty()) {
            authorClassify = item.author + " "
        }
        if (item.chapterName.isNotEmpty())
            authorClassify += item.chapterName
        holder.setText(R.id.tv_author_classify, authorClassify)
        // 日期
        holder.setText(R.id.tv_date, "时间:${item.niceDate}")
        // 点击取消收藏
        holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_collect)
    }
}