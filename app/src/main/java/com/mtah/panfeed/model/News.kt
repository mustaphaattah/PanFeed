package com.mtah.panfeed.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("status")
    private var status: String,

    @SerializedName("totalResults")
    private var totalResult: String,

    @SerializedName("articles")
    var articles: List<Article>
)
