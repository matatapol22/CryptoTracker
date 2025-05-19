package com.bignerdranch.android.cryptotracker

import com.github.mikephil.charting.data.Entry

class CryptoRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getCurrentPrice(cryptoId: String): Double {
        val response = apiService.getCryptoPrices(cryptoId)
        return response[cryptoId]?.get("usd") ?: 0.0
    }

    // Теперь возвращает список Entry (timestamp, price)
    suspend fun getHistoricalData(cryptoId: String, interval: String): List<List<Double>> {
        val response = apiService.getMarketChart(cryptoId, "usd", interval)
        return response.prices // [[timestamp, price], ...]
    }

    suspend fun getCoinDetails(id: String): CoinDetailsResponse {
        return apiService.getCoinDetails(id)
    }
}