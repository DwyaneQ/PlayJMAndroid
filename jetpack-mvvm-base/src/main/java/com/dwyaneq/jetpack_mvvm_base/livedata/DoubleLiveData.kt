package me.hgj.jetpackmvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 *  Created by DWQ on 2020/4/24.
 *  E-Mail:lomapa@163.com
 *  自定义的Double类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class DoubleLiveData(var value: Double = 0.0) : MutableLiveData<Double>(value) {

    override fun getValue(): Double {
        return super.getValue()!!
    }
}