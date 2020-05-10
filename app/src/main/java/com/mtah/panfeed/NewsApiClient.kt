package com.mtah.panfeed

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NewsApiClient {
    private val NEWSAPI_BASE_URL = "https://newsapi.org/v2/"
    private val GNEWS_BASE_URL = "https://gnews.io/api/v3/"
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(NEWSAPI_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build()


    fun<T> getApi(newsInt: Class<T> ): T{
        return retrofit.create(newsInt)
    }
}