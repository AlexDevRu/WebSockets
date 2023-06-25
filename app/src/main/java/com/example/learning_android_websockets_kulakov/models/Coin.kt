package com.example.learning_android_websockets_kulakov.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class Coin(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val priceUsd: String
) {
    fun getImageUrl() = "https://assets.coincap.io/assets/icons/${symbol.lowercase()}@2x.png"
}
