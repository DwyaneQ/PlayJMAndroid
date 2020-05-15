package com.dwyaneq.playandroidkotlin.adapter

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dwyaneq.jetpack_mvvm_base.utils.GlideImageLoader
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult

/**
 * Created by DWQ on 2020/4/30.
 * E-Mail:lomapa@163.com
 */
class HomeArticleAdapter : BaseMultiItemQuickAdapter<ArticleResult, BaseViewHolder>(),
    LoadMoreModule {
    private val article: Int = 1
    private val project: Int = 2

    init {
        addItemType(article, R.layout.item_home_article)
        addItemType(project, R.layout.item_home_project)
    }

    override fun getDefItemViewType(position: Int): Int {
        return if (TextUtils.isEmpty(data[position].envelopePic)) article else project
    }

    override fun convert(holder: BaseViewHolder, item: ArticleResult) {
        // 标题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY))
        } else {
            holder.setText(R.id.tv_title, Html.fromHtml(item.title))
        }
        // 作者
        if (item.author.isNotEmpty())
            holder.setText(R.id.tv_author, item.author)
        else
            holder.setText(R.id.tv_author, item.shareUser)
        // 分类
        holder.setText(R.id.tv_class, "${item.superChapterName}/${item.chapterName}")
        // 日期
        holder.setText(R.id.tv_date, item.niceShareDate)
        // 最新
        holder.setGone(R.id.tv_new_article, !item.fresh)
        // 置顶
        holder.setGone(R.id.tv_top_article, item.type != 1)
        // 是否收藏
        if (item.collect)
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_collect)
        else
            holder.setImageResource(R.id.iv_collect, R.drawable.ic_article_uncollect)
        // 标签
        val hasTag = item.tags.isNotEmpty()
        holder.setGone(R.id.tv_type_tag, !hasTag);
        if (hasTag) {
            holder.setText(R.id.tv_type_tag, item.tags[0].name)
        }
        when (holder.itemViewType) {
            article -> {
            }
            project -> {

                // 项目图片
                GlideImageLoader.loadImage(
                    holder.getView<ImageView>(R.id.iv_cover)
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
            }

        }
    }

}