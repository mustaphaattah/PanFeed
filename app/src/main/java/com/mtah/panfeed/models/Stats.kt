package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("result")
    val results: List<Case>
)