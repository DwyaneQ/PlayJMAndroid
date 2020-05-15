package com.dwyaneq.playandroidkotlin.module.project

import androidx.lifecycle.MutableLiveData
import com.dwyaneq.jetpack_mvvm_base.ext.request
import com.dwyaneq.jetpack_mvvm_base.state.ResultState
import com.dwyaneq.playandroidkotlin.common.CollectViewModel
import com.dwyaneq.playandroidkotlin.data.bean.ArticleResult
import com.dwyaneq.playandroidkotlin.data.bean.TabResult
import com.dwyaneq.playandroidkotlin.repository.ProjectRepository
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState

/**
 * Created by DWQ on 2020/5/12.
 * E-Mail:lomapa@163.com
 */
class ProjectViewModel : CollectViewModel() {
    private val projectRepository by lazy { ProjectRepository() }
    var projectTabData: MutableLiveData<ResultState<ArrayList<TabResult>>> =
        MutableLiveData()
    var projectArticleData: MutableLiveData<ListDataUiState<ArticleResult>> = MutableLiveData()
    var pageIndex: Int = 0

    fun getProjectTabList() {
        request({ projectRepository.getProjectTabList() }, projectTabData)
    }

    fun getProjectArticleList(id: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 0
        }
        request({
            if (id == -1) {
                projectRepository.getNewArticleList(pageIndex)
            } else {
                projectRepository.getProjectArticleList(pageIndex, id)
            }
        }
            , {
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
                projectArticleData.postValue(listDataUiState)
            }
            , {
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResult>()
                    )
                projectArticleData.postValue(listDataUiState)
            })
    }
}