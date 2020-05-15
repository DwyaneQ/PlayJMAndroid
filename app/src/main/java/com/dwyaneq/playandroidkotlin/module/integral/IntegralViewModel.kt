package com.dwyaneq.playandroidkotlin.module.integral

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.IntegralHistoryResult
import com.dwyaneq.playandroidkotlin.repository.IntegralRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/14.
 * E-Mail:lomapa@163.com
 */
class IntegralViewModel : BaseViewModel() {
    val repository by lazy { IntegralRepository() }
    var pageIndex = 0
    var integralData: MutableLiveData<ListDataUiState<IntegralHistoryResult>> = MutableLiveData()

    fun getIntegralHistory(isRefresh: Boolean = false) {
        if (isRefresh)
            pageIndex = 0
        request({ repository.getIntegralHistoryList(pageIndex) }, {
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
            integralData.postValue(listDataUiState)
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<IntegralHistoryResult>()
                )
            integralData.postValue(listDataUiState)
        })
    }
}