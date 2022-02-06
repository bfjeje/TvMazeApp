package com.ellerbach.tvmazeapp.ui.mainactivity

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel(), LifecycleObserver {

    private val _isSearching = MutableLiveData<Boolean>().apply { value = false }
    val isSearching: LiveData<Boolean>
        get() = _isSearching

    private val _query = MutableLiveData<String>().apply { value = "" }
    val query: LiveData<String>
        get() = _query

    fun search(query: String?) {
        _isSearching.postValue(true)
        _query.postValue(query)
    }
}