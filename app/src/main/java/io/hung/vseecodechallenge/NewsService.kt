package io.hung.vseecodechallenge

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET

interface NewsService {

    @GET("top-headlines?sources=bbc-sport")
    suspend fun getBBCSportNews(): NetworkResponse<TopHeadlinesResponse, ErrorResponse>
}