package com.dwyaneq.playandroidkotlin.aop

import androidx.fragment.app.Fragment

/**
 * Created by DWQ on 2020/8/3.
 * E-Mail:lomapa@163.com
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CheckLogin {
}