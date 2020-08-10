package com.mtah.panfeed.models

import com.google.gson.annotations.SerializedName

data class CountryList (
    var countries: MutableList<Country>
)