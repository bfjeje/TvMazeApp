package com.ellerbach.tvmazeapp.ui.mainactivity

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel(), LifecycleObserver {

    val clearSearchView = MutableLiveData<Boolean>(false)
    fun clearSearchView(value: Boolean) {
        this.clearSearchView.value = value
    }

    val query: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}