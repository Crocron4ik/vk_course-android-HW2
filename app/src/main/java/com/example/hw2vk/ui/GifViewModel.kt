package com.example.hw2vk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2vk.data.GifRepository
import kotlinx.coroutines.launch

class GifViewModel : ViewModel() {

    private val repository = GifRepository()
    val gifs = repository.gifs
    val isLoading = repository.isLoading
    val error = repository.error

    fun loadGifs() {
        viewModelScope.launch {
            repository.loadGifs(isRefresh = true)
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            repository.loadGifs(isRefresh = false)
        }
    }

    fun retry() {
        viewModelScope.launch {
            repository.retry()
        }
    }
}