package com.example.hw2vk.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GifApi {
    @GET("v1/gifs/trending")
    suspend fun getTrending(
        @Query("api_key") apiKey: String = "8fsnpO158VfyVdVLzEV6b0HysA2oUUKY",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): GiphyResponse
}