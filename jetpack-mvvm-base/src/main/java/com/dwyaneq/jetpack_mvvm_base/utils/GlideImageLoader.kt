package com.dwyaneq.jetpack_mvvm_base.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * Created by DWQ on 2020/4/30.
 * E-Mail:lomapa@163.com
 */
object GlideImageLoader {
    /**
     * 加载url图片
     */
    fun loadImage(
        imageView: ImageView,
        url: String
    ) {
        loadImage(imageView, url, isCrossFade = false, isCircle = false)
    }

    /**
     * 加载圆形url图片
     */
    fun loadCircleImage(
        imageView: ImageView,
        url: String
    ) {
        loadImage(imageView, url, isCrossFade = false, isCircle = true)
    }


    @SuppressLint("CheckResult")
    fun loadImage(
        imageView: ImageView,
        src: Any,
        isCrossFade: Boolean,
        isCircle: Boolean
    ) {
        val glideBuilder = Glide.with(imageView.context)
            .load(src)
            .apply(RequestOptions.centerCropTransform())
        if (isCrossFade) {
            glideBuilder.transition(DrawableTransitionOptions.withCrossFade(500))
        }
        if (isCircle) {
            glideBuilder.apply(RequestOptions.bitmapTransform(CircleCrop()))
        }
        glideBuilder
            .into(imageView)
    }
}