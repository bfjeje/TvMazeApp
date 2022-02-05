package com.ellerbach.tvmazeapp.data

import androidx.lifecycle.LiveData
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.model.ShowDAO
import com.ellerbach.tvmazeapp.network.ShowService

class ShowsRepository(val network: ShowService, val showDAO: ShowDAO) {

    val showList: LiveData<List<Show?>?> = showDAO.searchAll

    suspend fun refreshShows() {
        try {
            val result = network.searchAllShows()
            showDAO.save(result)
        } catch (cause: Throwable) {
            throw ShowsRefreshError("Unable to refresh shows", cause)
        }
    }
}

class ShowsRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)