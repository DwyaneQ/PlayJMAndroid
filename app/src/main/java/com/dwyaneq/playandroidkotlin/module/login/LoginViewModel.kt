package com.dwyaneq.playandroidkotlin.module.login

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.config.AppConfig
import com.dwyaneq.playandroidkotlin.common.utils.SharePreferenceUtils
import com.dwyaneq.playandroidkotlin.data.bean.UserInfo
import com.dwyaneq.playandroidkotlin.repository.LoginRepository
import org.jetbrains.anko.toast

/**
 * 登录模块的ViewModel
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 */
class LoginViewModel : BaseViewModel() {

    val usernameStorage: String by SharePreferenceUtils(AppConfig.USER_NAME, "")
    val userPwdStorage: String by SharePreferenceUtils(AppConfig.USER_PWD, "")

    val loginRepository: LoginRepository by lazy { LoginRepository() }

    /**
     * 用户名
     */
    val username = ObservableField<String>()

    /**
     * 密码
     */
    val userpwd = ObservableField<String>()

    /**
     * 确认密码
     */
    val repassword = ObservableField<String>()

    /**
     * 返回的登陆结果
     */
    val loginResult = MutableLiveData<ResultState<UserInfo>>()

    init {
        // 获取本地缓存的用户名密码
        username.set(usernameStorage)
        userpwd.set(userPwdStorage)
    }

    /**
     * 通过DataBinding在XML绑定的点击方法
     */
    fun onSubmitClick(view: View) {
        val username = this.username.get()
        val password = this.userpwd.get()

        username?.apply {
            if (this.isEmpty()) {
                view.context.toast(R.string.login_user_name_tips)
                return
            }
        }

        password?.apply {
            if (this.isEmpty()) {
                view.context.toast(R.string.login_user_pwd_tips)
                return
            }
        }
        Log.i("LoginInput", "username = $username--userpwd = $password")
        request(
            { loginRepository.login(this.username.get()!!.trim(), userpwd.get()!!.trim()) },
            loginResult,
            true,
            "正在登录..."
        )
    }

    fun onRegisterClick(view: View) {
        val username = this.username.get()
        val password = this.userpwd.get()
        val pwdConfirm = this.repassword.get()

        username?.apply {
            if (this.isEmpty()) {
                view.context.toast(R.string.login_user_name_tips)
                return
            }
        }

        password?.apply {
            if (this.isEmpty()) {
                view.context.toast(R.string.login_user_pwd_tips)
                return
            }

        }
        pwdConfirm?.apply {
            if (this.isEmpty()) {
                view.context.toast(R.string.login_user_pwd_confirm_tips)
                return
            }
            if (this != password) {
                view.context.toast(R.string.login_user_pwd_confirm_not_same_tips)
                return
            }
        }
        // 注册
        request(
            {
                loginRepository.register(
                    this.username.get()!!.trim(),
                    this.userpwd.get()!!.trim(),
                    this.repassword.get()!!.trim()
                )
            },
            loginResult,
            true,
            "正在注册..."
        )
    }
}