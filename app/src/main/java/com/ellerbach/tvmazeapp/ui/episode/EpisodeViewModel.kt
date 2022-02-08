package com.ellerbach.tvmazeapp.ui.episode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ellerbach.tvmazeapp.model.*

class EpisodeViewModel : ViewModel() {

    private val _episode = MutableLiveData<Episode>().apply { value = emptyEpisode }
    val episode: LiveData<Episode>
        get() = _episode

    fun setEpisode(episode: Episode) {
        this._episode.postValue(episode)
    }

    val emptyEpisode = Episode(
        0,
        "",
        "",
        0,
        0,
        "",
        "",
        "",
        "",
        0,
        Rating(0.0),
        Image("", ""),
        "",
        Links(Self(""))
    )
}