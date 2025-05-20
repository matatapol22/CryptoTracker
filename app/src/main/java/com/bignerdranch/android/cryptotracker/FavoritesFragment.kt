package com.bignerdranch.android.cryptotracker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: FavoriteCoinsAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FavoritesViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val adapter = FavoriteCoinsAdapter { selectedCoin ->
            sharedViewModel.selectCoin(selectedCoin)
            // Переключение на главную вкладку
            (activity as? MainActivity)?.switchToMainTab()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerFavorites)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter




        viewModel.favoriteCoins.observe(viewLifecycleOwner) { coins ->
            adapter.submitList(coins)
        }
    }
}