package com.dwyaneq.playandroidkotlin.ui.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by DWQ on 2020/5/8.
 * E-Mail:lomapa@163.com
 */
class FlowSpaceItemDecoration constructor(space: Int) : RecyclerView.ItemDecoration() {
    private var mSpace: Int = space
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = mSpace;
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
    }
}