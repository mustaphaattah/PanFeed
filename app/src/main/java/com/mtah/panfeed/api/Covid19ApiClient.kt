package com.mtah.panfeed.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Covid19ApiClient {
    private const val COVID19_DATA_BASE_URL = "https://covid-19-coronavirus-statistics2.p.rapidapi.com/countriesData/"

    private val retrofit = Retrofit.Builder().baseUrl(COVID19_DATA_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(NewsApiClient.client)
        .build()


    fun <T> getApi(caseInterface: Class<T>) : T {
        return retrofit.create(caseInterface)
    }

}