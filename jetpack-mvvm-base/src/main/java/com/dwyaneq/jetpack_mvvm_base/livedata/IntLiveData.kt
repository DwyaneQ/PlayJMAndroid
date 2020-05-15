package me.hgj.jetpackmvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 *  Created by DWQ on 2020/4/24.
 *  E-Mail:lomapa@163.com
 *  自定义的Int类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value) {

    override fun getValue(): Int {
        return super.getValue()!!
    }
}