package io.hung.vseecodechallenge

import retrofit2.http.GET

interface NewsService {

    @GET("top-headlines?sources=bbc-sport")
    suspend fun getBBCSportNews(): TopHeadlinesResponse
}