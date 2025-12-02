package com.example.hw2vk.data

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.hw2vk.BuildConfig

interface GifApi {
    @GET("v1/gifs/trending")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = BuildConfig.GIPHY_API_KEY,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): GiphyResponse
}