package com.dwyaneq.jetpack_mvvm_base.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 获取vm clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

fun <VM : BaseViewModel> AppCompatActivity.getViewmodel(): VM {
    return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
}





