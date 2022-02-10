package com.ellerbach.tvmazeapp.ui.home

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

    fun refreshShowList(): Flow<PagingData<Show>> {
        return repository.getShows().cachedIn(viewModelScope)
    }
}