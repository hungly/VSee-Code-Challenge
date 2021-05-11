package io.hung.vseecodechallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ItemListViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> get() = _newList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            newsRepository.getNews()
                .catch { }
                .collect {
                    if (it.isEmpty()) {
                        loadNews()
                        return@collect
                    }
                    _newList.value = it
                    _isLoading.value = false
                }
        }
    }

    fun loadNews() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getNewsFromApi()
        }
    }
}