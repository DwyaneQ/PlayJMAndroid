package com.dwyaneq.playandroidkotlin.common.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dwyaneq.jetpack_mvvm_base.Ktx
import com.dwyaneq.playandroidkotlin.MApplication
import com.dwyaneq.playandroidkotlin.common.AppViewModel


fun AppCompatActivity.getAppViewModel(): AppViewModel {
    (Ktx.app as MApplication).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun Fragment.getAppViewModel(): AppViewModel {
    (Ktx.app as MApplication).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}


