package com.example.learning_android_websockets_kulakov.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learning_android_websockets_kulakov.models.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("select * from coins")
    fun getCoins() : Flow<List<Coin>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoin(coin: Coin)
}