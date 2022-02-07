package com.ellerbach.tvmazeapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ellerbach.tvmazeapp.model.Show

class ShowViewModel : ViewModel() {

    val showData = MutableLiveData<Show?>()

    fun setShow(show: Show?) {
        this.showData.postValue(show)
    }
}