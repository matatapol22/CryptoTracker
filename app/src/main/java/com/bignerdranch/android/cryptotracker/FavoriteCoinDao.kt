package com.bignerdranch.android.cryptotracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCoinDao {
    @Query("SELECT * FROM favorite_coins")
    fun getAllFavorites(): LiveData<List<FavoriteCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coin: FavoriteCoin)

    @Delete
    suspend fun delete(coin: FavoriteCoin)
}