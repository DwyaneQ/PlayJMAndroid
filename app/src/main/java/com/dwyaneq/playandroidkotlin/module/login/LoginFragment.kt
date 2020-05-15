package com.dwyaneq.playandroidkotlin.module.login

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import me.hgj.jetpackmvvm.ext.util.setOnclickNoRepeat
import org.jetbrains.anko.support.v4.toast

/**
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding?.loginViewModel = viewModel
        setOnclickNoRepeat(tv_register, onClick = {
            navigationPopUpTo(it, R.id.action_login_to_register)
        })
    }

    override fun getTitleRes(): Int = R.string.login_title

    override fun createObserver() {
        viewModel.loginResult.observe(this, Observer { resultState ->
            parseState(resultState, {
                Log.i("LoginResult", "userInfo = $it")
                CacheUtil.setUser(it)
                appViewModel.userinfo.postValue(it)
                Navigation.findNavController(common_toolbar).navigateUp()
            }, {
                Log.i("LoginResult", "error = ${it.errCode}" + "--errorMsg = ${it.errorMsg}")
                toast(it.errorMsg)
            })

        })
    }

}