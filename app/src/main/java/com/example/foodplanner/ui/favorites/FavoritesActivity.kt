package com.example.foodplanner.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foodplanner.App
import com.example.foodplanner.databinding.ActivityFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    private val adapter = FavoritesAdapter { meal ->
        viewModel.removeFavorite(meal)
        Snackbar.make(binding.root, "${meal.strMeal} removed from favorites", Snackbar.LENGTH_LONG)
            .setAction("Undo") { viewModel.addFavorite(meal) }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val app = application as App
        viewModel = ViewModelProvider(
            this,
            FavoritesViewModelFactory(app.favoritesRepository)
        )[FavoritesViewModel::class.java]

        binding.recyclerFavorites.adapter = adapter

        // Room Flow → UI updates automatically, no network needed
        lifecycleScope.launch {
            viewModel.favorites.collect { meals ->
                adapter.submitList(meals)
                binding.emptyState.visibility =
                    if (meals.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}