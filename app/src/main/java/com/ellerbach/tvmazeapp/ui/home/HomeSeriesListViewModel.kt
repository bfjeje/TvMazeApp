package com.ellerbach.tvmazeapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.util.singleArgViewModelFactory
import kotlinx.coroutines.flow.Flow

class HomeSeriesListViewModel(private val repository: ShowsRepository) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::HomeSeriesListViewModel)
    }

    private val _spinner = MutableLiveData<Boolean>(false)
    private val _snackBar = MutableLiveData<String?>()

    fun refreshShowList(): Flow<PagingData<Show>> {
        return repository.getShows().cachedIn(viewModelScope)
    }
}