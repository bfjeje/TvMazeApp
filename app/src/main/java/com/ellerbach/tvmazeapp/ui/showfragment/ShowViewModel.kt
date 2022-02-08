package com.ellerbach.tvmazeapp.ui.showfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ellerbach.tvmazeapp.data.ShowsRepository
import com.ellerbach.tvmazeapp.model.Episode
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.util.singleArgViewModelFactory

class ShowViewModel(private val repository: ShowsRepository) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::ShowViewModel)
    }

    val showData = MutableLiveData<Show?>()
    fun setShow(show: Show?) {
        this.showData.postValue(show)
    }

    var listGroup = MutableLiveData<List<Episode?>>()
    suspend fun getEpisodes(id: String) {
        listGroup.postValue(repository.getEpisodes(id))
    }


}