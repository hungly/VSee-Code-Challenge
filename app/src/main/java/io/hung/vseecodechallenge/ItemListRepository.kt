package io.hung.vseecodechallenge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface ItemListRepository {

    fun getNews(): Flow<List<News>>
}

class ItemListRepositoryImpl(private val newsService: NewsService) : ItemListRepository {

    override fun getNews(): Flow<List<News>> = flow {
        emit(newsService.getBBCSportNews().articles)
    }.flowOn(Dispatchers.IO)
}