package com.dwyaneq.playandroidkotlin.common

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.BaseViewModel
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.data.bean.CollectionResult
import com.dwyaneq.playandroidkotlin.repository.CollectRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.CollectUiState
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/11.
 * E-Mail:lomapa@163.com
 */
open class CollectViewModel : BaseViewModel() {
    val collectRepository by lazy { CollectRepository() }

    //收藏文章
    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    //我的收藏数据
    var collectionData: MutableLiveData<ListDataUiState<CollectionResult>> = MutableLiveData()


    var pageNo = 0

    fun collect(id: Int) {
        request({ collectRepository.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.postValue(uiState)
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = id)
            collectUiState.postValue(uiState)
        })
    }

    fun cancelCollect(id: Int) {
        request({ collectRepository.cancelCollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.postValue(uiState)
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUiState.postValue(uiState)
        })
    }

    fun getCollectionList(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ collectRepository.getCollectionList(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            collectionData.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<CollectionResult>()
                )
            collectionData.postValue(listDataUiState)
        })
    }

    fun cancelCollection(id: Int, originId: Int = -1) {
        request({ collectRepository.cancelCollection(id, originId) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.postValue(uiState)
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUiState.postValue(uiState)
        })
    }
}