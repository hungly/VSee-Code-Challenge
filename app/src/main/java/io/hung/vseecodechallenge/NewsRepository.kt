package io.hung.vseecodechallenge

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
        val newsFromApi = newsService.getBBCSportNews().articles
        newsDao.updateNewsList(newsFromApi)
    }
}