package com.dwyaneq.jetpack_mvvm_base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dwyaneq.jetpack_mvvm_base.ext.getVmClazz

/**
 * 基类Activity
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var dataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDataBinding()
        setContentView(getLayoutId())
        viewModel = createViewModel()
        registerDefUIChange()
        initView(savedInstanceState)
        createViewModel()
    }

    /**
     * 反射创建ViewMoel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this) as Class<VM>]
    }

    private fun createDataBinding(): ViewDataBinding {
        return DataBindingUtil.setContentView(this, getLayoutId())
    }

    abstract fun getLayoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        viewModel.uiChange.showDialog.observe(this, Observer {
            showLoading()
        })
        viewModel.uiChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }

    open fun dismissLoading() {
    }

    /**
     * String = "xx"形参声明时给予默认值，调用时可以不传参，使用默认值
     */
    open fun showLoading(msg: String = "正在加载...") {
    }
}
