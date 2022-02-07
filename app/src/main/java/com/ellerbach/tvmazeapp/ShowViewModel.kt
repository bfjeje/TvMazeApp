package com.ellerbach.tvmazeapp

import androidx.lifecycle.ViewModel
import com.ellerbach.tvmazeapp.model.Show

class ShowViewModel : ViewModel() {

    lateinit var showData: Show

    fun setShow(show: Show) {
        this.showData = show
    }


//    fun searchSpecificShow(query: String): List<SearchSpecificShow?> {
//        return repository.searchShow(query)
//    }

//    suspend fun getShowData(): {
//        TODO("Not yet implemented")
//    }
}