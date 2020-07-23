package com.mtah.panfeed.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiClient {
    private const val NEWSAPI_BASE_URL = "https://newsapi.org/v2/"
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder().baseUrl(NEWSAPI_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build()


    fun<T> getApi(newsInterface: Class<T> ): T{
        return retrofit.create(newsInterface)
    }
}