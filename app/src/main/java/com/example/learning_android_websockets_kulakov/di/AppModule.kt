package com.example.learning_android_websockets_kulakov.di

import android.content.Context
import androidx.room.Room
import com.example.learning_android_websockets_kulakov.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.learning_android_websockets_kulakov.api.CoinCapApi
import com.example.learning_android_websockets_kulakov.database.AppDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoinCapApi(
        @ApplicationContext app: Context
    ) : CoinCapApi {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(ChuckerInterceptor.Builder(app).build())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.COIN_CAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .build()

        return retrofit.create(CoinCapApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "coins_db"
    ).build()

    @Singleton
    @Provides
    fun provideCoinDao(db: AppDatabase) = db.getCoinsDao()

}