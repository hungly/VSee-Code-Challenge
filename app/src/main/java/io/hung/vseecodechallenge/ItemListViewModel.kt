package io.hung.vseecodechallenge

import androidx.lifecycle.ViewModel

class ItemListViewModel(private val itemListRepository: ItemListRepository) : ViewModel() {

    val newsList = itemListRepository.getNews()
}