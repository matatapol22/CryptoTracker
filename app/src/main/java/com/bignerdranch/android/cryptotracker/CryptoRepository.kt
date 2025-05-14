package com.bignerdranch.android.cryptotracker

class CryptoRepository {
    suspend fun getPrices(): Map<String, Map<String, Double>> {
        return RetrofitInstance.api.getPrices("bitcoin,ethereum", "usd")
    }
}