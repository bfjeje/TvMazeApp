package com.ellerbach.tvmazeapp.data

import androidx.paging.PagingData
import com.ellerbach.tvmazeapp.model.Show
import kotlinx.coroutines.flow.Flow

interface ShowsRemoteDataSource {
    fun getShows(): Flow<PagingData<Show>>
}
