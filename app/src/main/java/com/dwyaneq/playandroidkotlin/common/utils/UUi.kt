package com.dwyaneq.playandroidkotlin.common.utils

import android.app.Activity
import android.util.SparseArray
import android.view.View
import com.dwyaneq.playandroidkotlin.MApplication

/**
 * UI类工具
 * Created by shidawei on 16/8/4.
 */
object UUi {
    /**
     * 获取view
     *
     * @param activity
     * @param mViews
     * @param id
     * @param <T>
     * @return
    </T> */
    @Deprecated("")
    fun <T : View?> getView(
        activity: Activity,
        mViews: SparseArray<View?>,
        id: Int
    ): T? {
        var view = mViews[id] as T?
        if (view == null) {
            view = activity.findViewById<View>(id) as T
            mViews.put(id, view)
        }
        return view
    }

    fun <T : View?> getView(
        mView: View,
        mViews: SparseArray<View?>,
        id: Int
    ): T? {
        var view = mViews[id] as T?
        if (view == null) {
            view = mView.findViewById<View>(id) as T
            mViews.put(id, view)
        }
        return view
    }

    /**
     * 给多个view添加点击事件
     *
     * @param listener
     * @param views
     */
    @Deprecated("")
    fun setOnClickListener(
        listener: View.OnClickListener?,
        vararg views: View
    ) {
        if (views == null) {
            return
        }
        for (view in views) {
            view.setOnClickListener(listener)
        }
    }
    //    /**
    //     * 弹出自定义toast
    //     * @param msg
    //     * @param duration
    //     */
    //    public static void toast(Activity activity, String msg, int duration) {
    //        if(activity==null){
    //            return;
    //        }
    //        View layout = activity.getLayoutInflater().inflate(R.layout.deleber_toast, null);
    //        TextView txt = (TextView) layout.findViewById(R.id.main_toast_text);
    //        txt.setText(msg);
    //        Toast toast = new Toast(activity);
    //        //设置Toast的位置
    //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    //        toast.setDuration(duration);
    //        //让Toast显示为我们自定义的样子
    //        toast.setView(layout);
    //        toast.show();
    //
    //    }
    /**
     * dip 转 px
     *
     * @param dipValue
     * @return
     */
    fun dip2px(dipValue: Float): Int {
        val scale: Float =
            MApplication.instance.getResources().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     *
     * @param pxValue
     * @return
     */
    fun px2dip(pxValue: Float): Int {
        val scale: Float =
            MApplication.instance.getResources().getDisplayMetrics().density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale: Float =
            MApplication.instance.getResources().getDisplayMetrics().scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue
     * @return
     */
    fun sp2px(spValue: Float): Int {
        val fontScale: Float =
            MApplication.instance.getResources().getDisplayMetrics().scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}