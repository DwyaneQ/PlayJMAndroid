package com.dwyaneq.playandroidkotlin.module.wxgzh

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.repository.WXGZHRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
class WXGZHViewModel : CollectViewModel() {
    var pageIndex: Int = 0
    val wxgzhRepository by lazy { WXGZHRepository() }
    var tabData: MutableLiveData<ResultState<ArrayList<TabResult>>> = MutableLiveData()
    var wxArticleData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()

    fun getWXTabList() {
        request({ wxgzhRepository.getWXTabList() }, tabData)
    }

    fun getWXArticleList(id: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
        }
        request({ wxgzhRepository.getWXArticleList(id, pageIndex) }, {
            pageIndex++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            wxArticleData.postValue(listDataUiState)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResult>()
                )
            wxArticleData.postValue(listDataUiState)
        })
    }
}