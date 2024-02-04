package com.android.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.search.data.Constants
import com.android.search.data.KakaoImage
import com.android.search.retrofit.NetWorkClient
import com.android.search.retrofit.NetWorkInterface
import kotlinx.coroutines.launch

class MainViewModel(private val retrofit: NetWorkInterface) : ViewModel() {
    //저장할 라이브 데이터
//    private var _searchResults = MutableLiveData<List<KakaoImage>>()
    var _searchResults : MutableLiveData<List<KakaoImage>> = MutableLiveData(mutableListOf<KakaoImage>())
    val searchResult: LiveData<List<KakaoImage>>
        get() = _searchResults
    var _myItmes : MutableLiveData<MutableList<KakaoImage>> = MutableLiveData(mutableListOf<KakaoImage>())
    val myItmes : LiveData<MutableList<KakaoImage>>
        get() = _myItmes

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
                imgNetwork.image_search(Constants.HEADER, searchKeyword, "recency", 80)
            val responseVideo = imgNetwork.video_search(Constants.HEADER, searchKeyword, "recency", 15)

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
            Log.d("MainViewModel", "#aaa searchitems = ${searchItems.size}")
            Log.d("MainViewModel", "#aaa searchresult = ${_searchResults.value?.size}")

        }
    }

    fun likeItemView() {
        myItmes.value
    }

    fun likeItem(item: KakaoImage) {
        if (_myItmes.value?.contains(item) == true) {
            _myItmes.value?.remove(item)
        }else {
            _myItmes.value?.add(item)
        }
        Log.d("MainViewModel","#aaa like = ${myItmes.value}")
    }

//    fun unLikeItem(item : KakaoImage) {
//        _myItmes.value?.
//    }

//    fun likeItemToggle(item : KakaoImage) {
//        _myItmes.value?.let {
//
//        }
//    }

//    fun viewLikeItems(item: List<KakaoImage>) {
//        item.map { it.url ==  }
//
//
//    }
}






