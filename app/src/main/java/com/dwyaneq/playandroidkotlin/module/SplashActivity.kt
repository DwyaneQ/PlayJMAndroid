package com.dwyaneq.playandroidkotlin.module

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.module.main.MainActivity

/**
 * Created by DWQ on 2020/4/24.
 * E-Mail:lomapa@163.com
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 针对OPPO手机切换后台时会重新打开Splash问题的解决
        if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish()
            return
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (CacheUtil.isNightMode()) {
            //  开启黑夜模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            //  关闭黑夜模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    var mHandler = Handler()
    var splashHandler = SplashHandler()
    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(splashHandler, 2000)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
    }

    inner class SplashHandler : Runnable {
        override fun run() {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    /**
     * 当前为登录页面时，启用返回按键
     */
    override fun onBackPressed() {
    }
}