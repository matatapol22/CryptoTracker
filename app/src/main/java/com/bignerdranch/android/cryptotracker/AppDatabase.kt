package com.bignerdranch.android.cryptotracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCoin::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCoinDao(): FavoriteCoinDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_database"
                ).build().also { instance = it }
            }
    }
}