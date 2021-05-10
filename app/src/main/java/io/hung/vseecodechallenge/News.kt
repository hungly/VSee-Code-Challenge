package io.hung.vseecodechallenge

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class News(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String
)
