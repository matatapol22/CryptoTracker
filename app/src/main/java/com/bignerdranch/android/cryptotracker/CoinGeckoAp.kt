package com.bignerdranch.android.cryptotracker

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("simple/price")
    suspend fun getCryptoPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String = "usd"
    ): Map<String, Map<String, Double>>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: String
    ): MarketChartResponse

    @GET("coins/{id}")
    suspend fun getCoinDetails(
        @Path("id") id: String
    ): CoinDetailsResponse

}




