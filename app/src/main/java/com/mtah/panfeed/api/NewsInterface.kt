package com.mtah.panfeed.api

import com.mtah.panfeed.models.News
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
//NEWS api
    @GET("top-headlines")
    fun getLocalNews(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int
    ) : Call<News>

    @GET("everything")
    fun getAllCovidNews(
        @Query("apiKey") apiKey: String,
        @Query("q") keyWord: String,
        @Query("language") language: String,
        @Query("pageSize") pageSize: Int,
        @Query("sortBy") sort: String
    ) : Call<News>

}