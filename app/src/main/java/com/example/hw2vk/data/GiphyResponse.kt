package com.example.hw2vk.data

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    @SerializedName("data") val data: List<GifItem>
)

data class GifItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: Images
)

data class Images(
    @SerializedName("fixed_height") val fixed_height: ImageData
)

data class ImageData(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String
)
