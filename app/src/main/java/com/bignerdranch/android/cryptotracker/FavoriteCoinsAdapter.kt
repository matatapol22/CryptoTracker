package com.bignerdranch.android.cryptotracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.cryptotracker.databinding.ItemFavoriteCoinBinding
import com.bumptech.glide.Glide

class FavoriteCoinsAdapter(
    private val onItemClick: (FavoriteCoin) -> Unit
) : ListAdapter<FavoriteCoin, FavoriteCoinsAdapter.FavoriteCoinViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_coin, parent, false)
        return FavoriteCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteCoinViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin)
    }

    inner class FavoriteCoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coinName: TextView = itemView.findViewById(R.id.textViewCoinName)
        fun bind(coin: FavoriteCoin) {
            coinName.text = coin.name
            itemView.setOnClickListener {
                onItemClick(coin)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteCoin>() {
        override fun areItemsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin) = oldItem == newItem
    }
}