package io.hung.vseecodechallenge

import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<List<News>>

    suspend fun getNewsFromApi()
}

class ItemListRepositoryImpl(
    private val newsDao: NewsDao,
    private val newsService: NewsService
) : NewsRepository {

    private val newsList = newsDao.getAllNews()

    override fun getNews(): Flow<List<News>> = newsList

    override suspend fun getNewsFromApi() {
        when (val response = newsService.getBBCSportNews()) {
            is NetworkResponse.Success -> response.body.articles?.let { newsDao.updateNewsList(it) }
            is NetworkResponse.ServerError -> throw response.error
            is NetworkResponse.NetworkError -> throw response.error
            is NetworkResponse.UnknownError -> throw response.error
        }
    }
}