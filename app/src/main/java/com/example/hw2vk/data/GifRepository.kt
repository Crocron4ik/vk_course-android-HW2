package com.example.hw2vk.data

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GifRepository {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.giphy.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GifApi::class.java)

    val gifs = mutableStateOf(emptyList<GifItem>())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    private var offset = 0

    suspend fun loadGifs(isRefresh: Boolean = false) {
        isLoading.value = true
        error.value = null

        try {
            val response = withContext(Dispatchers.IO) {
                api.getTrending(offset = if (isRefresh) 0 else offset)
            }

            if (isRefresh) {
                gifs.value = response.data
                offset = response.data.size
            } else {
                gifs.value = gifs.value + response.data
                offset += response.data.size
            }
        } catch (e: Exception) {
            error.value = e.message ?: "Ошибка загрузки"
        } finally {
            isLoading.value = false
        }
    }

    suspend fun retry() {
        offset = 0
        loadGifs(isRefresh = true)
    }
}