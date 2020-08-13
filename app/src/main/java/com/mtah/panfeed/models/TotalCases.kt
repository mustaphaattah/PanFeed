package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class TotalCases(
    @SerializedName("confirmed")
    var confirmed: Int,

    @SerializedName("recovered")
    var recovered: Int,

    @SerializedName("critical")
    var critical: Int,

    @SerializedName("deaths")
    var deaths: Int,

    @SerializedName("lastChange")
    var lastChange: String,

    @SerializedName("lastUpdate")
    var lastUpdate: String
)