package com.android.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.search.retrofit.NetWorkInterface

class ViewModelFactory (private val retrofit: NetWorkInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(retrofit) as T
    }
}