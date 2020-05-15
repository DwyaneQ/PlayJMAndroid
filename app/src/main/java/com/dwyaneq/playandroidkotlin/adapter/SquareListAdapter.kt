package com.dwyaneq.playandroidkotlin.adapter

import android.os.Build
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SquareListAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<ArticleResult, BaseViewHolder>(layoutRes), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: ArticleResult) {
        // 标题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY))
        } else {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title))
        }
        // 最新
        holder.setGone(R.id.tv_new_article, !item.fresh)
        // 分享人
        if (item.author.isNotEmpty())
            holder.setText(R.id.tv_author, "分享人:${item.author}  ${item.niceShareDate}")
        else
            holder.setText(R.id.tv_author, "分享人:${item.shareUser}  ${item.niceShareDate}")
        // 是否收藏
        if (item.collect)
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_collect)
        else
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_uncollect)
    }
}