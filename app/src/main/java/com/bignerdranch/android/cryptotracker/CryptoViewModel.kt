package com.bignerdranch.android.cryptotracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import java.security.KeyStore
import kotlin.text.toFloat

class CryptoViewModel : ViewModel() {
    private val repository = CryptoRepository()

    private val _cryptoPrice = MutableLiveData<Double>()
    val cryptoPrice: LiveData<Double> = _cryptoPrice

    private val _historicalData = MutableLiveData<List<Entry>>()
    val historicalData: LiveData<List<Entry>> = _historicalData

    fun getCurrentPrice(cryptoId: String) {
        viewModelScope.launch {
            val result = repository.getCurrentPrice(cryptoId)
            _cryptoPrice.postValue(result)
        }
    }

    fun getHistoricalData(cryptoId: String, interval: String) {
        viewModelScope.launch {
            val priceList = repository.getHistoricalData(cryptoId, interval)

            val entries = priceList.map { point ->
                val timestamp = point[0].toFloat()  // UNIX time (usually in milliseconds)
                val price = point[1].toFloat()      // Price value
                Entry(timestamp, price)
            }

            _historicalData.postValue(entries)
        }
    }

    private val _coinDetails = MutableLiveData<CoinDetailsResponse>()
    val coinDetails: LiveData<CoinDetailsResponse> = _coinDetails

    fun getCoinDetails(id: String) {
        viewModelScope.launch {
            try {
                val details = repository.getCoinDetails(id)  // ← вот здесь было api
                _coinDetails.postValue(details)
            } catch (e: Exception) {
                Log.e("CryptoViewModel", "Ошибка при загрузке иконки: ${e.message}")
            }
        }
    }
}


