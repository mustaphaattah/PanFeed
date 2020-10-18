package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class Case(
    @SerializedName("country")
    var country: String,

    @SerializedName("totalCases")
    var totalCases: String,

    @SerializedName("newCases")
    var newCases: String,

    @SerializedName("totalDeaths")
    var totalDeaths: String,

    @SerializedName("newDeaths")
    var newDeaths: String,

    @SerializedName("totalRecovered")
    var totalRecovered: String,

    @SerializedName("activeCases")
    var activeCase: String
)

//"country":"North America"
//"totalCases":"500,901"
//"newCases":"+681"
//"totalDeaths":"17,694"
//"newDeaths":"+28"
//"totalRecovered":"32,299"
//"activeCases":"450,908"
