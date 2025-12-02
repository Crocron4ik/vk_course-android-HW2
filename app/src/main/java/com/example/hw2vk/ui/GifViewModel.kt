package com.example.hw2vk.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw2vk.data.GifRepository
import kotlinx.coroutines.launch

class GifViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GifRepository(application.applicationContext)
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