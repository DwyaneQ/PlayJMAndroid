package com.dwyaneq.playandroidkotlin.aop

import android.util.Log
import androidx.fragment.app.Fragment
import com.dwyaneq.jetpack_mvvm_base.navigation.NavHostFragment
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature


/**
 * Created by DWQ on 2020/8/3.
 * E-Mail:lomapa@163.com
 */
@Aspect
class CheckLoginAspect {
    private val TAG = "CheckLogin"

    /**
     * 找到需要处理的切点
     */
    @Pointcut("execution(@com.dwyaneq.playandroidkotlin.aop.CheckLogin  * *(..))")
    fun executionCheckLogin() {

    }

    @Around("executionCheckLogin()")
    fun checkLogin(joinPoint: ProceedingJoinPoint): Any? {
        val signature =
            joinPoint.signature as MethodSignature
        val checkLogin = signature.method.getAnnotation(CheckLogin::class.java)
        if (checkLogin != null) {
            val fragment: Fragment = joinPoint.getThis() as Fragment
            return if (CacheUtil.isLogin()) {
                Log.i(TAG, "checkLogin: 登录成功 ")
                joinPoint.proceed()
            } else {
                Log.i(TAG, "checkLogin: 请登录")
                NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_main_to_login)
                null
            }
        }
        return joinPoint.proceed()
    }
}