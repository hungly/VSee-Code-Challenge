package io.hung.vseecodechallenge.network

import com.haroldadmin.cnradapter.NetworkResponse
import io.hung.vseecodechallenge.network.response.ErrorResponse
import io.hung.vseecodechallenge.network.response.TopHeadlinesResponse
import retrofit2.http.GET

interface NewsService {

    @GET("top-headlines?sources=bbc-sport")
    suspend fun getBBCSportNews(): NetworkResponse<TopHeadlinesResponse, ErrorResponse>
}