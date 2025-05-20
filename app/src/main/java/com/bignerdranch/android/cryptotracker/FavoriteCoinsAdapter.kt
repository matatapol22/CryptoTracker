package com.bignerdranch.android.cryptotracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.cryptotracker.databinding.ItemFavoriteCoinBinding
import com.bumptech.glide.Glide

class FavoriteCoinsAdapter(
    private val onItemClick: (FavoriteCoin) -> Unit,
    private val onDeleteClick: (FavoriteCoin) -> Unit
) : ListAdapter<FavoriteCoin, FavoriteCoinsAdapter.FavoriteCoinViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_coin, parent, false)
        return FavoriteCoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteCoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteCoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coinName: TextView = itemView.findViewById(R.id.textViewCoinName)
        private val icon = itemView.findViewById<ImageView>(R.id.imageViewCoinIcon)
        private val deleteBtn = itemView.findViewById<ImageView>(R.id.buttonDeleteCoin)
        fun bind(coin: FavoriteCoin) {
            Glide.with(itemView.context).load(coin.imageUrl).into(icon)
            coinName.text = coin.name
            itemView.setOnClickListener {
                onItemClick(coin)
            }

            deleteBtn.setOnClickListener {
                onDeleteClick(coin)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteCoin>() {
        override fun areItemsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FavoriteCoin, newItem: FavoriteCoin) = oldItem == newItem
    }
}