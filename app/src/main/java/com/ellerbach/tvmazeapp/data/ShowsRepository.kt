package com.ellerbach.tvmazeapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.model.ShowDAO
import com.ellerbach.tvmazeapp.network.ShowService
import kotlinx.coroutines.flow.Flow

class ShowsRepository(private val showService: ShowService, private val showDAO: ShowDAO) :
    ShowsRemoteDataSource {

    val showList: Flow<List<Show?>?> = showDAO.searchAll

    override fun getShows(): Flow<PagingData<Show>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 249
            ),
            pagingSourceFactory = {
                AllShowsPagingSource(service = showService, showDAO = showDAO)
            }
        ).flow
    }

    suspend fun searchShow(query: String): List<SearchSpecificShow?> {
        return showService.searchSpecificShow(query)
    }
}

class ShowsRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
