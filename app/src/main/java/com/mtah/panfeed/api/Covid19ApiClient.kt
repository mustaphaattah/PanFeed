package com.mtah.panfeed.api

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Covid19ApiClient {
    private const val COVID19_DATA_BASE_URL = "https://covid-19-tracking.p.rapidapi.com/v1/"

    private val retrofit = Retrofit.Builder().baseUrl(COVID19_DATA_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(NewsApiClient.client)
        .build()


    fun <T> getApi(caseInterface: Class<T>) : T {
        return retrofit.create(caseInterface)
    }

}