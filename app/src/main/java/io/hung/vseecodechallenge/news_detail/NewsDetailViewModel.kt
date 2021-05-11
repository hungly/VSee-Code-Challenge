package io.hung.vseecodechallenge.news_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsDetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun isLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}