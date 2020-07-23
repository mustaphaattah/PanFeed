package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("Country_text")
    var country: String,

    @SerializedName("Total Cases_text")
    var cases: String,

    @SerializedName("Total Recovered_text")
    var recovered: String,

    @SerializedName("Total Deaths_text")
    var deaths: String,

    @SerializedName("New Deaths_text")
    var newDeaths: String,

    @SerializedName("New Cases_text")
    var newCases: String,

    @SerializedName("Last Update")
    var lastUpdate: String
)