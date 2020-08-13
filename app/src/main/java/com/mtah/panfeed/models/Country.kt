package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("Active Cases_text")
    var active: String,

    @SerializedName("Country_text")
    var country: String,

    @SerializedName("Last Update")
    var lastUpdate: String,

    @SerializedName("New Cases_text")
    var newCases: String,

    @SerializedName("New Deaths_text")
    var newDeaths: String,

    @SerializedName("Total Cases_text")
    var cases: String,

    @SerializedName("Total Deaths_text")
    var deaths: String,

    @SerializedName("Total Recovered_text")
    var recovered: String
)

//"Active Cases_text": "6,385,577",
//"Country_text": "World",
//"Last Update": "2020-08-09 16:39",
//"New Cases_text": "+123,047",
//"New Deaths_text": "+2,909",
//"Total Cases_text": "19,918,124",
//"Total Deaths_text": "731,705",
//"Total Recovered_text": "12,800,842"