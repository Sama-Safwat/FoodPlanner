package com.example.foodplanner.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.repository.FavoritesRepository
import com.example.foodplanner.databinding.ActivityFavoritesBinding
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    private val adapter = FavoritesAdapter(
        onItemClick = { meal ->
            val fragment = MealDetailsFragment.newInstance(meal.idMeal)
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit()
        },
        onRemove = { meal ->
            viewModel.removeFavorite(meal)
            Snackbar.make(binding.root, "${meal.strMeal} removed", Snackbar.LENGTH_LONG)
                .setAction("Undo") { viewModel.addFavorite(meal) }
                .show()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dao = AppDatabase.getDatabase(this).favoritesDao()
        val repository = FavoritesRepository(dao)

        viewModel = ViewModelProvider(
            this,
            FavoritesViewModelFactory(repository)
        )[FavoritesViewModel::class.java]

        binding.recyclerFavorites.adapter = adapter

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