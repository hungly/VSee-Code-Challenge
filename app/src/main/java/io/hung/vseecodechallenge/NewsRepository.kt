package io.hung.vseecodechallenge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

interface NewsRepository {

    fun getNews(): Flow<List<News>>
}

class ItemListRepositoryImpl(private val newsService: NewsService) : NewsRepository {

    override fun getNews(): Flow<List<News>> = flow {
        Timber.d("Get from API")
        emit(newsService.getBBCSportNews().articles)
    }.flowOn(Dispatchers.IO)
}