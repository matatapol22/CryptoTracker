package com.bignerdranch.android.cryptotracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coins")
data class FavoriteCoin(
    @PrimaryKey val id: String, // например: "bitcoin"
    val name: String,
    val imageUrl: String?
)