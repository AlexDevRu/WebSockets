package com.example.learning_android_websockets_kulakov.api

import com.example.learning_android_websockets_kulakov.dto.CoinCapAssetsResponse
import retrofit2.http.GET

interface CoinCapApi {
    @GET("assets")
    suspend fun getAllAssets() : CoinCapAssetsResponse
}