package com.mtah.panfeed.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.mtah.panfeed.models.Country
import com.mtah.panfeed.models.CountryList
import com.mtah.panfeed.models.TotalCases
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CasesInterface {

    @Headers(
        "x-rapidapi-host: covid-19-tracking.p.rapidapi.com",
        "x-rapidapi-key: 5356e51509msh652378a34ed5795p159defjsne19741d2bb22"
        ,"useQueryString: true"
    )
    @GET("https://covid-19-tracking.p.rapidapi.com/v1")
    fun getAllCases () : Call<List<Country>>
}