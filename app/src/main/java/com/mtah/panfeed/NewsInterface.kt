package com.mtah.panfeed

import com.mtah.panfeed.models.News
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("top-headlines")
    fun getLocalNews(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("q") keyWord: String,
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