package com.ellerbach.tvmazeapp.ui.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    var isSearching = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun search(query: String) {
        isSearching.value = true
    }

    data class MainViewState(
        val isSearching: Boolean = false,
//        val query
    )
}