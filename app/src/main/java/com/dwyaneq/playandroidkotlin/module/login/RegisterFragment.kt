package com.dwyaneq.playandroidkotlin.module.login

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.common_toolbar.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/5/9.
 * E-Mail:lomapa@163.com
 */
class RegisterFragment : BaseFragment<LoginViewModel, FragmentRegisterBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun createObserver() {
        viewModel.loginResult.observe(this, Observer { result ->
            parseState(result, {
                Log.i("LoginResult", "userInfo = $it")
                CacheUtil.setUser(it)
                appViewModel.userinfo.postValue(it)
                Navigation.findNavController(common_toolbar)
                    .navigate(R.id.action_search_result_to_webview)
            }, {
                Log.i("LoginResult", "error = ${it.errCode}" + "--errorMsg = ${it.errorMsg}")
                toast(it.errorMsg)
            })
        })
    }

    override fun getTitleRes(): Int  = R.string.register_title
}