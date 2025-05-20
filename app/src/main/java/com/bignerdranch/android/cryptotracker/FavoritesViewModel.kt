package com.bignerdranch.android.cryptotracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.favoriteCoinDao()

    val favoriteCoins: LiveData<List<FavoriteCoin>> = dao.getAllFavorites()

    fun addFavorite(coin: FavoriteCoin) {
        viewModelScope.launch {
            dao.insert(coin)
        }
    }

    fun removeFavorite(coin: FavoriteCoin) {
        viewModelScope.launch {
            dao.delete(coin)
        }
    }


}