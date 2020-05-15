package com.dwyaneq.playandroidkotlin.module.setting

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.network.NetworkApi
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.databinding.FragmentSettingBinding
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_setting.*
import me.hgj.jetpackmvvm.ext.util.setOnclickNoRepeat
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

/**
 * Created by DWQ on 2020/5/15.
 * E-Mail:lomapa@163.com
 */
class SettingFragment : BaseFragment<SettingViewModel, FragmentSettingBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding?.settingViewModel = viewModel
        setOnclickNoRepeat(rl_clean_cache, onClick = {
            when (it.id) {
                R.id.rl_clean_cache -> {// 清除缓存
                    viewModel.cleanCache()
                    context?.let { it1 ->
                        AlertDialog.Builder(it1)
                            .setMessage(R.string.logout_tips)
                            .setTitle(R.string.setting_clean_cache_tips)
                            .setPositiveButton(
                                "确定"
                            ) { _, _ ->
                                viewModel.cleanCache()
                            }
                            .setNeutralButton("取消", null)
                            .create()
                            .show()
                    }
                }
            }
        })
        switch_night_mode.isChecked = CacheUtil.isNightMode()
        switch_night_mode.onCheckedChange { _, isChecked ->
            CacheUtil.setNightMode(isChecked)
            if (isChecked) {
                //  开启黑夜模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                //  关闭黑夜模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_setting

    override fun getTitleRes(): Int = R.string.setting_title

    override fun lazyLoadData() {
        viewModel.getCacheSize()
    }
}