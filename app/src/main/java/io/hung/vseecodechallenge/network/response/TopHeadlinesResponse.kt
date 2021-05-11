package io.hung.vseecodechallenge.network.response

import com.squareup.moshi.JsonClass
import io.hung.vseecodechallenge.model.News

@JsonClass(generateAdapter = true)
data class TopHeadlinesResponse(val articles: List<News>?)
