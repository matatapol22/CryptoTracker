package com.bignerdranch.android.cryptotracker

class CryptoRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getCurrentPrice(cryptoId: String): Double {
        val response = apiService.getCryptoPrices(cryptoId)
        return response[cryptoId]?.get("usd") ?: 0.0
    }

    suspend fun getHistoricalData(cryptoId: String, interval: String): List<Double> {
        val response = apiService.getMarketChart(cryptoId, "usd", interval)
        return response.prices.map { it[1] } // Список цен
    }
}