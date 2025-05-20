package com.bignerdranch.android.cryptotracker

import com.google.gson.annotations.SerializedName

data class CoinDetailsResponse(
    val id: String,
    val symbol: String,
    val name: String,
    val image: ImageData,
    val marketData: MarketData?
)

data class ImageData(
    val thumb: String,
    val small: String,
    val large: String
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>
)
