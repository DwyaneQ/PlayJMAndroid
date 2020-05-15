package com.dwyaneq.playandroidkotlin.module.search

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.HotKeyResult
import com.dwyaneq.playandroidkotlin.repository.SearchRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/7.
 * E-Mail:lomapa@163.com
 */
class SearchViewModel : CollectViewModel() {
    var searchHistoryData: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var hotKeyData: MutableLiveData<ResultState<ArrayList<HotKeyResult>>> = MutableLiveData()

    val searchRepository: SearchRepository by lazy { SearchRepository() }

    var searchResultList: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()

    var pageIndex: Int = 0

    fun getSearchHistory() {
        searchHistoryData.postValue(searchRepository.getSearchHistory())
    }

    fun getHotKeyList() {
        request(
            {
                searchRepository.getHotKeyList()
            },
            hotKeyData
        )
    }

    fun getSearchList(isRefresh: Boolean, key: String) {
        if (isRefresh) {
            pageIndex = 0
        }
        request(
            {
                searchRepository.getSearchList(pageIndex, key)
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
                searchResultList.postValue(listDataUiState)
            }, {
//请求失败
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResult>()
                    )
                searchResultList.postValue(listDataUiState)
            }
        )
    }
}