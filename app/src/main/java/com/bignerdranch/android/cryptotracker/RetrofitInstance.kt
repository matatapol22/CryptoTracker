package com.bignerdranch.android.cryptotracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CoinGeckoApi by lazy {
        retrofit.create(CoinGeckoApi::class.java)
    }
} ..//