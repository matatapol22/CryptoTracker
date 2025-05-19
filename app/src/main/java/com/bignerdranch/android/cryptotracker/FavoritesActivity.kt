package com.bignerdranch.android.cryptotracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.cryptotracker.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var adapter: FavoriteCoinsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        adapter = FavoriteCoinsAdapter()
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = adapter

        favoritesViewModel.favoriteCoins.observe(this) { coins ->
            adapter.submitList(coins)
        }
    }
}