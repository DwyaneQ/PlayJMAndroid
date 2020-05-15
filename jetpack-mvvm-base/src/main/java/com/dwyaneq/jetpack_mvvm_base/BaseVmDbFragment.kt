package com.dwyaneq.jetpack_mvvm_base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.dwyaneq.jetpack_mvvm_base.ext.autoCleared
import com.dwyaneq.jetpack_mvvm_base.ext.getVmClazz
import com.dwyaneq.jetpack_mvvm_base.navigation.NavHostFragment

/**
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 * Fragment基类，自动注入ViewModel和DataBinding
 */
abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    /**
     * 根据Fragment动态清理和获取binding对象
     */
    var binding by autoCleared<DB>()

    lateinit var viewModel: VM

    private var isFirstVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = createViewModel()
        initView(savedInstanceState)
        onVisible()
        registerDefUIChange()
        initData()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    open fun initData() {

    }

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建ViewModel
     * todo 可修改为dagger注入
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this) as Class<VM>]
    }

    abstract fun getLayoutId(): Int

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirstVisible) {
            lazyLoadData()
            isFirstVisible = false
            createObserver()
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {

    }

    /**
     * 创建观察者
     */
    open fun createObserver() {

    }

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        viewModel.uiChange.showDialog.observe(viewLifecycleOwner, Observer {
            showLoading()
        })
        viewModel.uiChange.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoading()
        })
    }

    abstract fun dismissLoading()

    /**
     * String = "xx"形参声明时给予默认值，调用时可以不传参，使用默认值
     */
    abstract fun showLoading(msg: String = "正在加载...")

    /**
     * Navigation 的页面跳转
     */
    open fun navigationPopUpTo(
        view: View,
        actionId: Int,
        args: Bundle? = null,
        finishStack: Boolean = false
    ) {
        NavHostFragment.findNavController(this).navigate(actionId, args)
        if (finishStack) {
            activity?.finish()
        }
    }
}