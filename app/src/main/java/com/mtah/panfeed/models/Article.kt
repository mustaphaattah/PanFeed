package com.mtah.panfeed.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "news_table")
data class Article (

    @SerializedName("title")
    var title: String,

    @SerializedName("url")
    var url: String,

    @SerializedName("urlToImage")
    var urlToImage: String,

    @SerializedName("publishedAt")
    var publishedAt: String

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Ignore
    @SerializedName("source")
    lateinit var source: Source

    @Ignore
    @SerializedName("author")
    lateinit var author: String

    @Ignore
    @SerializedName("description")
    lateinit var description: String

    @Ignore
    @SerializedName("content")
    lateinit var content: String

}
