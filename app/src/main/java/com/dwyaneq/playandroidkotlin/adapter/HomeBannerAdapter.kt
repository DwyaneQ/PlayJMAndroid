package com.dwyaneq.playandroidkotlin.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dwyaneq.jetpack_mvvm_base.utils.GlideImageLoader
import com.dwyaneq.playandroidkotlin.data.bean.BannerResult
import com.youth.banner.adapter.BannerAdapter


/**
 * Created by DWQ on 2020/4/30.
 * E-Mail:lomapa@163.com
 */
class HomeBannerAdapter(datas: ArrayList<BannerResult>) :
    BannerAdapter<BannerResult, BannerViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: BannerResult,
        position: Int,
        size: Int
    ) {
        GlideImageLoader.loadImage(holder.imageView, data.imagePath)
    }
}

class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView = view

}