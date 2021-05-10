package io.hung.vseecodechallenge

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopHeadlinesResponse(val articles: List<News>)
