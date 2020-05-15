package com.dwyaneq.playandroidkotlin.module.search

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.dwyaneq.jetpack_mvvm_base.ext.parseState
import com.dwyaneq.playandroidkotlin.R
import com.dwyaneq.playandroidkotlin.adapter.SearchHistoryAdapter
import com.dwyaneq.playandroidkotlin.adapter.SearchHotKeyAdapter
import com.dwyaneq.playandroidkotlin.common.base.BaseFragment
import com.dwyaneq.playandroidkotlin.common.ext.init
import com.dwyaneq.playandroidkotlin.common.utils.CacheUtil
import com.dwyaneq.playandroidkotlin.common.utils.UUi
import com.dwyaneq.playandroidkotlin.databinding.FragmentSearchBinding
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowLayoutManager
import com.dwyaneq.playandroidkotlin.ui.recyclerview.FlowSpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.*
import me.hgj.jetpackmvvm.ext.util.toJson
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by DWQ on 2020/5/7.
 * E-Mail:lomapa@163.com
 */
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {
    var searchHistoryAdapter = SearchHistoryAdapter(R.layout.item_search_history)
    var searchHotKeyAdapter = SearchHotKeyAdapter(R.layout.item_search_history)

    override fun initView(savedInstanceState: Bundle?) {
        rv_history.init(FlowLayoutManager(), searchHistoryAdapter)
        rv_history.addItemDecoration(
            FlowSpaceItemDecoration(UUi.dip2px(6f))
        )
        searchHistoryAdapter.let {
            it.addChildClickViewIds(R.id.iv_delete)
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_delete) {
                    viewModel.searchHistoryData.value?.let { searchHistoryList ->
                        searchHistoryList.removeAt(position)
                        viewModel.searchHistoryData.postValue(searchHistoryList)
                    }
                }
            }
            it.setOnItemClickListener { _, view, position ->
                val queryStr = searchHistoryAdapter.data[position]
                val data = Bundle()
                data.putString("search_key", queryStr)
                navigationPopUpTo(view, R.id.action_search_to_search_result, data)
            }
        }
        rv_hotkey.init(FlowLayoutManager(), searchHotKeyAdapter)
        rv_hotkey.addItemDecoration(
            FlowSpaceItemDecoration(UUi.dip2px(6f))
        )
        searchHotKeyAdapter.setOnItemClickListener { _, view, position ->
            //  搜索
            val queryStr = searchHotKeyAdapter.data[position].name
            val data = Bundle()
            data.putString("search_key", queryStr)
            navigationPopUpTo(view, R.id.action_search_to_search_result, data)
        }
        search_view.run {
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            isSubmitButtonEnabled = true
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(search_view) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //  搜索
                    query?.let {
                        updateKey(it)
                    }
                    val data = Bundle()
                    data.putString("search_key", query)
                    navigationPopUpTo(search_view, R.id.action_search_to_search_result, data)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // 文字改变
                    return false
                }

            })
        }
        iv_delete_all.onClick {
            viewModel.searchHistoryData.postValue(arrayListOf())
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun lazyLoadData() {
        // 获取搜索历史
        viewModel.getHotKeyList()
        viewModel.getSearchHistory()
    }

    override fun createObserver() {
        viewModel.searchHistoryData.observe(this, Observer {
            searchHistoryAdapter.setList(it)

            CacheUtil.setSearchHistoryData(it.toJson())
        })

        viewModel.hotKeyData.observe(this, Observer {
            parseState(it
                , { hotkeyList ->
                    searchHotKeyAdapter.setNewInstance(hotkeyList)
                })
        })
    }

    /**
     */
    private fun updateKey(keyStr: String) {
        viewModel.searchHistoryData.value?.let {
            if (it.contains(keyStr)) {
                //当搜索历史中包含该数据时 删除
                it.remove(keyStr)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, keyStr)
            viewModel.searchHistoryData.postValue(it)
        }
    }

}