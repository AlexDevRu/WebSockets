package com.example.learning_android_websockets_kulakov.ui.available_coins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android_websockets_kulakov.api.CoinCapApi
import com.example.learning_android_websockets_kulakov.database.CoinDao
import com.example.learning_android_websockets_kulakov.models.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableCoinsViewModel @Inject constructor(
    private val coinCapApi: CoinCapApi,
    private val coinDao: CoinDao
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _coins = MutableLiveData<List<Coin>>()
    val coins : LiveData<List<Coin>> = _coins

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)
                _coins.postValue(coinCapApi.getAllAssets().data)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun insertCoin(coin: Coin) {
        viewModelScope.launch(Dispatchers.IO) {
            coinDao.insertCoin(coin)
            _finish.emit(Unit)
        }
    }
}