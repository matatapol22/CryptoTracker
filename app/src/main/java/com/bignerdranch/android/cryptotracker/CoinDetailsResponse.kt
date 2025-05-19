package com.bignerdranch.android.cryptotracker

data class CoinDetailsResponse(
    val id: String,
    val symbol: String,
    val name: String,
    val image: ImageData
)

data class ImageData(
    val thumb: String,
    val small: String,
    val large: String
)
