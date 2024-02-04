package com.android.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.search.data.KakaoImage

class MyboxViewModel : ViewModel() {
    private var _myItems = MutableLiveData<List<KakaoImage>>()
    val myItems : LiveData<List<KakaoImage>>
        get() = _myItems


}