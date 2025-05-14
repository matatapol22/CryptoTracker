package com.bignerdranch.android.cryptotracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import java.security.KeyStore

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
            val entries = priceList.mapIndexed { index, value ->
                Entry(index.toFloat(), value.toFloat())
            }
            _historicalData.postValue(entries)
        }
    }
}
