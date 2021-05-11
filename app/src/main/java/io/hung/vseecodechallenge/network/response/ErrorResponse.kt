package io.hung.vseecodechallenge.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(val status: String?, val code: String, val message: String)