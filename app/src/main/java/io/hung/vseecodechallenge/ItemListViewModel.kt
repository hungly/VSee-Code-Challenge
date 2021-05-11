package io.hung.vseecodechallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ItemListViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> get() = _newList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<String?>(null)
    val isError: LiveData<String?> get() = _isError

    init {
        viewModelScope.launch {
            newsRepository.getNews()
                .catch { handleException(it) }
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

    fun clearError() {
        _isError.value = null
    }

    fun loadNews() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                newsRepository.getNewsFromApi()
            } catch (exception: Exception) {
                handleException(exception) {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun handleException(error: Throwable, postAction: () -> Unit = { }) {
        viewModelScope.launch {
            Timber.e(error)
            _isError.value = error.message
            postAction()
        }
    }
}