package me.hgj.base.databind

import androidx.databinding.ObservableField

/**
 *  Created by DWQ on 2020/4/24.
 *  E-Mail:lomapa@163.com
 *  自定义的Double类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class DoubleObservableField(value: Double = 0.0) : ObservableField<Double>(value) {

    override fun get(): Double {
        return super.get()!!
    }

}