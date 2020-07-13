package com.dwyaneq.playandroidkotlin.module.webview

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.DialogFragmentNavigator
import com.dwyaneq.jetpack_mvvm_base.navigation.NavHostFragment
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.databinding.FragmentWebviewBinding
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_webview.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by DWQ on 2020/5/7.
 * E-Mail:lomapa@163.com
 */
class WebViewFragment : BaseFragment<WebViewViewModel, FragmentWebviewBinding>() {
    private var mAgentWeb: AgentWeb? = null

    lateinit var url: String

    lateinit var articleTitle: String

    override fun initView(savedInstanceState: Bundle?) {
        iv_right_btn.setImageResource(R.drawable.ic_web_refresh)
    }

    override fun initData() {
        url = arguments?.getString("article_url")!!
        articleTitle = arguments?.getString("article_title")!!
    }

    override fun getLayoutId(): Int = R.layout.fragment_webview

    override fun lazyLoadData() {
        // 标题
        tv_title.text = articleTitle
        //加载网页
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(lly_webview_content, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)

        //添加返回键逻辑
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let {
                        if (it.webCreator.webView.canGoBack()) {
                            it.webCreator.webView.goBack()
                        } else {
                            NavHostFragment.findNavController(this@WebViewFragment).navigateUp()
                        }
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        lly_back.onClick {
            mAgentWeb?.let {
//                if (it.webCreator.webView.canGoBack()) {
//                    it.webCreator.webView.goBack()
//                } else {
                    NavHostFragment.findNavController(this@WebViewFragment).navigateUp()
//                }
            }
        }
        ll_btn.let {
            ll_btn.visibility = View.VISIBLE
            ll_btn.onClick {
                //刷新
                mAgentWeb?.urlLoader?.reload()
            }
        }
    }


    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        (activity as? AppCompatActivity)?.setSupportActionBar(null)
        super.onDestroy()
    }
}