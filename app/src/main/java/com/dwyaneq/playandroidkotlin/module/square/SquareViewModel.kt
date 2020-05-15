package com.dwyaneq.playandroidkotlin.module.square

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.repository.SquareRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/13.
 * E-Mail:lomapa@163.com
 */
class SquareViewModel : CollectViewModel() {
    var pageIndex = 0
    val squareData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()
    val squareRepository by lazy { SquareRepository() }

    fun getSquareList(isRefresh: Boolean = false) {
        if (isRefresh)
            pageIndex = 0
        request({ squareRepository.getSquareList(pageIndex) }, {
            pageIndex++
            var listDataUiState =
                ListDataUiState<ArticleResult>(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            squareData.postValue(listDataUiState)
        }, {
            pageIndex++
            var listDataUiState =
                ListDataUiState<ArticleResult>(
                    isSuccess = true,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf()
                )
            squareData.postValue(listDataUiState)
        })
    }
}