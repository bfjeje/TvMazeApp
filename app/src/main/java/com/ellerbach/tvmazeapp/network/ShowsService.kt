package com.ellerbach.tvmazeapp.network

import com.ellerbach.tvmazeapp.model.SearchSpecificShow
import com.ellerbach.tvmazeapp.model.Show
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val URL_BASE = "https://api.tvmaze.com/"
private val service: ShowService by lazy {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(ShowService::class.java)
}

fun getNetworkService() = service

interface ShowService {

    @GET("shows")
    suspend fun searchAllShows(
        @Query("page") page: Int = 0
    ): List<Show>

    @GET("/search/shows")
    suspend fun searchSpecificShow(
        @Query("q") page: String = ""
    ): List<SearchSpecificShow>
}