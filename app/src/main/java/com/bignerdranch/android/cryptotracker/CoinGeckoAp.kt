package com.bignerdranch.android.cryptotracker

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("simple/price")
    suspend fun getPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String
    ): Map<String, Map<String, Double>>
}