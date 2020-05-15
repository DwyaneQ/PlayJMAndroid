package com.dwyaneq.playandroidkotlin.adapter

import android.os.Build
import android.text.Html
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.jetpack_mvvm_base.utils.GlideImageLoader
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.WXArticleResult

/**
 * Created by DWQ on 2020/5/12.
 * E-Mail:lomapa@163.com
 */
class ProjectArticleAdapter constructor(layoutRes: Int) :
    BaseQuickAdapter<ArticleResult, BaseViewHolder>(layoutRes), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: ArticleResult) {
        // 标题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY))
        } else {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title))
        }
        // 项目图片
        GlideImageLoader.loadImage(
            holder.getView(R.id.iv_cover)
            , item.envelopePic
        )
        // 描述
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.setText(
                R.id.tv_desc,
                Html.fromHtml(item.desc, Html.FROM_HTML_MODE_LEGACY)
            )
        } else {

            holder.setText(R.id.tv_desc, Html.fromHtml(item.desc))
        }
        // 作者 日期
        var authorDate = if (item.author.isNotEmpty())
            item.author + " " + item.niceShareDate
        else
            item.author + " " + item.shareUser
        holder.setText(R.id.tv_date_author, authorDate)
        // 是否收藏
        if (item.collect)
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_collect)
        else
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_uncollect)
    }
}