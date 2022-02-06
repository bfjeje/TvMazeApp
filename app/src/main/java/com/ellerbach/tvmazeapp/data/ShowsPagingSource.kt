package com.ellerbach.tvmazeapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ellerbach.tvmazeapp.model.Show
import com.ellerbach.tvmazeapp.model.ShowDAO
import com.ellerbach.tvmazeapp.network.ShowService
import retrofit2.HttpException
import java.io.IOException

private const val SHOWS_STARTING_PAGE_INDEX = 0
const val NETWORK_PAGE_SIZE = 248

class ShowsPagingSource(
    private val service: ShowService,
    val showDAO: ShowDAO
) : PagingSource<Int, Show>() {

    override fun getRefreshKey(state: PagingState<Int, Show>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Show> {
        val pageIndex = params.key ?: SHOWS_STARTING_PAGE_INDEX
        return try {
            val showsResponse = service.searchAllShows(
                page = pageIndex
            )
            showDAO.save(showsResponse)
            val nextKey =
                if (showsResponse.isEmpty()) {
                    null
                } else {
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = showsResponse,
                prevKey = if (pageIndex == SHOWS_STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}