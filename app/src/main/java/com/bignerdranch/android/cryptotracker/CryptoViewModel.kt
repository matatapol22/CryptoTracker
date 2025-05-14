package com.bignerdranch.android.cryptotracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {
    private val repository = CryptoRepository()
    private val _prices = MutableLiveData<Map<String, Map<String, Double>>>()
    val prices: LiveData<Map<String, Map<String, Double>>> = _prices

    init {
        fetchPrices()
    }

    private fun fetchPrices() {
        viewModelScope.launch {
            try {
                val result = repository.getPrices()
                _prices.postValue(result)
            } catch (e: Exception) {
                Log.e("CryptoViewModel", "Error: ${e.message}")
            }
        }
    }
}