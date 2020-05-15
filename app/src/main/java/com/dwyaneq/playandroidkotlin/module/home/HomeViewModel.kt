package com.dwyaneq.playandroidkotlin.module.home

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.BannerResult
import com.dwyaneq.playandroidkotlin.repository.HomeRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/4/29.
 * E-Mail:lomapa@163.com
 */
class HomeViewModel : CollectViewModel() {

    private var pageIndex: Int = 0

    var homeBannerData: MutableLiveData<ResultState<ArrayList<BannerResult>>> = MutableLiveData()

    var homeArticleData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()

    val homeRepository by lazy { HomeRepository() }

    fun getBannerList() {
        request(
            {
                homeRepository.getBannerList()
            },
            homeBannerData
        )
    }

    fun getArticleList(isRefresh: Boolean) {
        if (isRefresh) {
            pageIndex = 0
        }
        request(
            {
                homeRepository.getHomeArticleList(pageIndex)
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
                homeArticleData.postValue(listDataUiState)
            }, {
//请求失败
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResult>()
                    )
                homeArticleData.postValue(listDataUiState)
            }
        )
    }
}