package com.bignerdranch.android.cryptotracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedCoin = MutableLiveData<FavoriteCoin>()
    val selectedCoin: LiveData<FavoriteCoin> get() = _selectedCoin

    fun selectCoin(coin: FavoriteCoin) {
        _selectedCoin.value = coin
    }
}