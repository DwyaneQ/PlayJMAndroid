package me.hgj.jetpackmvvm.databind

import androidx.databinding.ObservableField

/**
 *  Created by DWQ on 2020/4/24.
 *  E-Mail:lomapa@163.com
 *  自定义的Boolean类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

}