package com.mtah.panfeed.api

import com.mtah.panfeed.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface CasesInterface {

    @Headers(
        "x-rapidapi-host: covid-19-tracking.p.rapidapi.com",
        "x-rapidapi-key: 5356e51509msh652378a34ed5795p159defjsne19741d2bb22"
        ,"useQueryString: true"
    )
    @GET("https://covid-19-tracking.p.rapidapi.com/v1")
    fun getAllCases () : Call<MutableList<Country>>
}