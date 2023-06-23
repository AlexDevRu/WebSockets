package com.example.learning_android_websockets_kulakov

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.IO.Options
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber

class MainViewModel : ViewModel(), Emitter.Listener {

    private val _messages = MutableLiveData<List<Pair<Boolean, String>>>()
    val messages : LiveData<List<Pair<Boolean, String>>> = _messages

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Timber.e("opened")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Timber.e("onMessage $text")
            _messages.value = messages.value.orEmpty() + (false to text)
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

    private val okHttpClient = OkHttpClient()
    private val webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)

    //val options = Options.builder().setTransports(arrayOf("websocket")).setPath("/v3/").build()
    //val options1 = IO.Options()
    //val socket = IO.socket("wss://fcsapi.com")

    fun init() {
        /*val event = Event( "subscribe", "ETH-USD,BTC-USD")
        val json = Gson().toJson(event)
        webSocket.send(json)*/

        //val event2 = Event("real_time_join", "1,2,3")
        //val json2 = Gson().toJson(event2)
        //webSocket.send(json2)


        /*socket.connect()
        socket.emit("heartbeat", "QyK5GGvp4eYU1SXb5ciNe")
        socket.emit("real_time_join", "1,1984,80,81,7774,7778")
        socket.on("data_received", this)
        socket.on("successfully") {
            Timber.e("SUCCESS")
        }
        socket.on("message") { args ->
            Timber.e("message ${args.map { it.toString() }}")
        }
        socket.on("connect_error") { args ->
            Timber.e("connect_error ${args.map { it.toString() }}")
        }*/

        viewModelScope.launch {
            repeat(3) {
                val text = "test$it"
                webSocket.send(text)
                _messages.postValue(messages.value.orEmpty() + (true to text))
                delay(2000)
            }
        }
    }

    override fun call(vararg args: Any?) {
        Timber.e(args.map { it.toString() }.toString())
    }

    private fun createRequest(): Request {
        val websocketURL = "wss://s9291.nyc1.piesocket.com/v3/1?api_key=OXiMRUOF0F2l8RCLS7uXY70q8fOsBMXpy419CJkw"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

}