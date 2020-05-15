package com.dwyaneq.playandroidkotlin.module.system

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.SystemTabResult
import com.dwyaneq.playandroidkotlin.repository.SystemRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class SystemViewModel : CollectViewModel() {

    val repository by lazy { SystemRepository() }

    val systemTabData: MutableLiveData<ResultState<ArrayList<SystemTabResult>>> = MutableLiveData()

    var articleData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()

    var pageIndex: Int = 0
    fun getSystemTab() {
        request({ repository.getSystemTabList() }, systemTabData)
    }

    fun getSystemArticleList(cid: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
        }
        request(
            {
                repository.getSystemArticleList(pageIndex, cid)
            }, {
                //请求成功
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
                articleData.postValue(listDataUiState)
            }, {
//请求失败
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResult>()
                    )
                articleData.postValue(listDataUiState)
            }
        )
    }
}