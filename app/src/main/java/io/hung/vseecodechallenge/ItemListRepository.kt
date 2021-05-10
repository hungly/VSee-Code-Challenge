package io.hung.vseecodechallenge

interface ItemListRepository {
}

class ItemListRepositoryImpl(private val newsService: NewsService) : ItemListRepository {

}