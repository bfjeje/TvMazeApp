package com.ellerbach.tvmazeapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellerbach.tvmazeapp.data.ShowsRefreshError
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.util.singleArgViewModelFactory
import kotlinx.coroutines.launch

class HomeSeriesListViewModel(private val repository: ShowsRepository) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::HomeSeriesListViewModel)
    }

    private val _spinner = MutableLiveData<Boolean>(false)
    private val _snackBar = MutableLiveData<String?>()
    val listOfShows = repository.showList

    fun refreshShowList() {
        viewModelScope.launch {
            try {
                _spinner.value = true
                repository.refreshShows()
            } catch (error: ShowsRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }

}