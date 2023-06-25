package com.example.learning_android_websockets_kulakov.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learning_android_websockets_kulakov.models.Coin

@Database(
    entities = [Coin::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCoinsDao(): CoinDao
}