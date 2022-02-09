package com.ellerbach.tvmazeapp.ui.searchfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.util.singleArgViewModelFactory

class SearchShowViewModel(private val repository: ShowsRepository) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::SearchShowViewModel)
    }

    var listGroup = MutableLiveData<List<SearchSpecificShow?>>()
    suspend fun updateShows(query: String) {
        listGroup.postValue(repository.searchShow(query))
    }
}