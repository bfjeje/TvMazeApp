package com.ellerbach.tvmazeapp.ui.showfragment

import android.text.Html
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    val name: LiveData<String> = Transformations.map(showData) {
        it?.name
    }

    val isSummaryPresent: LiveData<Boolean> = Transformations.map(showData) {
        !it?.summary?.isBlank()!!
    }

    val showSummary: LiveData<String> = Transformations.map(showData) {
        "Summary: \n${convertSummaryHTML(it?.summary)}"
    }

    private fun convertSummaryHTML(summary: String?): Spanned? =
        summary?.let { safeSummary ->
            Html.fromHtml(safeSummary, Html.FROM_HTML_MODE_COMPACT)
        }


    val showImage: LiveData<String> = Transformations.map(showData) {
        it?.image?.medium
    }

    var listGroup = MutableLiveData<List<Episode?>>()
    suspend fun getEpisodes(id: String) {
        listGroup.postValue(repository.getEpisodes(id))
    }

    val showTimeAndGenres: LiveData<Boolean> = Transformations.switchMap(showData) { show ->
        Transformations.switchMap(isShowGenres) { genres ->
            Transformations.map(isShowSchedule) { schedule ->
                genres ?: false && schedule ?: false
            }
        }
    }

    val isShowGenres: LiveData<Boolean?> = Transformations.map(showData) {
        it?.genres?.isNotEmpty()
    }

    val genres: LiveData<List<String?>?> = Transformations.map(showData) { show ->
        show?.genres
    }

    val isShowSchedule: LiveData<Boolean?> = Transformations.map(showData) { show ->
        show?.schedule?.days?.isNotEmpty() ?: false && show?.schedule?.time?.isNotEmpty() ?: false
    }
    val days: LiveData<List<String?>?> = Transformations.map(showData) { show ->
        show?.schedule?.days
    }

    val time: LiveData<String?> = Transformations.map(showData) { show ->
        show?.schedule?.time
    }

}