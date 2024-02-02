package com.android.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.search.data.Constants
import com.android.search.data.Image
import com.android.search.data.KakaoImage
import com.android.search.data.Video
import com.android.search.retrofit.NetWorkClient
import com.android.search.retrofit.NetWorkInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val retrofit: NetWorkInterface) : ViewModel() {
    //저장할 라이브 데이터
    private var _searchResults = MutableLiveData<List<KakaoImage>>()
    val searchResult: LiveData<List<KakaoImage>>
        get() = _searchResults

    fun getDataWithKeyword(keyword: String) {
        doSearch(keyword)
    }

    //검색
    private fun doSearch(searchKeyword: String) {
        var searchItems: MutableList<KakaoImage> = mutableListOf()
        val imgNetwork = NetWorkClient.imgNetWork
        viewModelScope.launch {
            searchItems.clear()
            val responseImage =
                imgNetwork.image_search(Constants.HEADER, searchKeyword, "recency", 2)
            val responseVideo = imgNetwork.video_search(Constants.HEADER, searchKeyword, "recency", 2)

            responseImage.documents.forEach{
                val kakaoImg = KakaoImage(Constants.SEARCH_TYPE_IMAGE,it.displaySitename,it.datetime,it.thumbnailUrl)
                searchItems.add(kakaoImg)
            }
            responseVideo.documents.forEach {
                val kakaoVideo =
                    KakaoImage(Constants.SEARCH_TYPE_VIDEO, it.author, it.datetime, it.thumbnail)
                searchItems.add(kakaoVideo)
            }
            _searchResults.value = searchItems
            Log.d("SearchViewModel", "#aaa searchitems = ${searchItems.size}")
            Log.d("SearchViewModel", "#aaa searchresult = ${_searchResults.value?.size}")

        }
    }
}





