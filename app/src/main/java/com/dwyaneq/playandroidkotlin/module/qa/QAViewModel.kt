package com.dwyaneq.playandroidkotlin.module.qa

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.repository.QARepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class QAViewModel : CollectViewModel() {
    private var pageIndex: Int = 0
    var qaData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()

    val repository by lazy { QARepository() }

    fun getQAList(isRefresh: Boolean) {
        if (isRefresh) {
            pageIndex = 0
        }
        request(
            {
                repository.getQAList(pageIndex)
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
                qaData.postValue(listDataUiState)
            }, {
//请求失败
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResult>()
                    )
                qaData.postValue(listDataUiState)
            }
        )
    }
}