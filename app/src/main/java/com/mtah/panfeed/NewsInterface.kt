package com.mtah.panfeed

import com.mtah.panfeed.model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("q") keyWord: String
    ) : Call<News>

    @GET("everything")
    fun getAllCovidNews(
        @Query("apiKey") apiKey: String,
        @Query("q") keyWord: String
    ) : Call<News>
}