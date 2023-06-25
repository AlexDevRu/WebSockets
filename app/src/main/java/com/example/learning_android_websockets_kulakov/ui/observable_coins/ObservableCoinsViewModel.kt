package com.example.learning_android_websockets_kulakov.ui.observable_coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android_websockets_kulakov.database.CoinDao
import com.example.learning_android_websockets_kulakov.models.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ObservableCoinsViewModel @Inject constructor(
    coinDao: CoinDao
) : ViewModel() {

    private val okHttpClient = OkHttpClient()

    private var webSocket : WebSocket? = null

    val coins = coinDao.getCoins().onEach {
        if (it.isNotEmpty())
            webSocket = okHttpClient.newWebSocket(createRequest(it), webSocketListener)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Timber.e("opened")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Timber.e("onMessage $text")
            //_messages.postValue(messages.value.orEmpty() + (false to text))
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Timber.e("onClosed: $code $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Timber.e("socket onFailure ${t.message}")
        }
    }

    private fun createRequest(coins: List<Coin>): Request {
        val websocketURL = "wss://ws.coincap.io/prices?assets=${coins.joinToString(",") { it.id }}"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }
}