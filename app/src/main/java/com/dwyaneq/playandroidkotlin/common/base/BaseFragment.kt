package com.dwyaneq.playandroidkotlin.common.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.BaseVmDbFragment
import com.dwyaneq.playandroidkotlin.common.AppViewModel
import com.dwyaneq.playandroidkotlin.common.ext.getAppViewModel
import com.dwyaneq.playandroidkotlin.ui.loading.CustomDialog
import com.etl.jiangxiappraisal.utils.TKeyboard
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import kotlinx.android.synthetic.main.common_toolbar.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by DWQ on 2020/4/28.
 * E-Mail:lomapa@163.com
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>
    : BaseVmDbFragment<VM, DB>(), SimpleImmersionOwner {
    val appViewModel: AppViewModel by lazy { getAppViewModel() }

    private var dialog: CustomDialog? = null
    open fun getDialog(): CustomDialog? {
        if (dialog == null) {
            dialog = CustomDialog.instance(activity)
            dialog?.setCancelable(false)
        }
        return dialog
    }

    override fun dismissLoading() {
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }
    }

    override fun showLoading(msg: String) {
        getDialog()?.show()
        getDialog()?.setTvProgress(msg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun initImmersionBar() {
        val immersionBar = ImmersionBar.with(this)
            .transparentStatusBar()
        if (common_toolbar != null) {
            immersionBar
                .titleBar(common_toolbar, false)
        }
        immersionBar.init()
    }

    open fun isBackBtnVisible(): Boolean = true

    private fun initToolBar() {
        tv_title?.text = getTitle()
        if (isBackBtnVisible()) {
            lly_back?.visibility = View.VISIBLE
            lly_back?.onClick {
                TKeyboard.hideKeyBoard(activity)
                Navigation.findNavController(common_toolbar).navigateUp()
            }
        } else {
            lly_back?.visibility = View.GONE
        }
    }

    open fun getTitle(): String {
        return if (getTitleRes() == -1) {
            ""
        } else
            getString(getTitleRes())
    }

    open fun getTitleRes(): Int = -1

    override fun navigationPopUpTo(
        view: View,
        actionId: Int,
        args: Bundle?,
        finishStack: Boolean
    ) {
        super.navigationPopUpTo(view, actionId, args, finishStack)
        TKeyboard.hideKeyBoard(activity)
    }
}