package com.bignerdranch.android.cryptotracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.cryptotracker.databinding.ItemFavoriteCoinBinding
import com.bumptech.glide.Glide

class FavoriteCoinsAdapter :
    ListAdapter<FavoriteCoin, FavoriteCoinsAdapter.FavoriteCoinViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCoinViewHolder {
        val binding = ItemFavoriteCoinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteCoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteCoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteCoinViewHolder(private val binding: ItemFavoriteCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: FavoriteCoin) {
            binding.textViewCoinName.text = coin.name
            Glide.with(binding.imageViewCoinIcon.context)
                .load(coin.imageUrl)
                .into(binding.imageViewCoinIcon)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteCoin>() {
        override fun areItemsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin): Boolean {
            return oldItem == newItem
        }
    }
}