package com.example.foodplanner.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<MealEntity>>(emptyList())
    val favorites: StateFlow<List<MealEntity>> = _favorites.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavorites().collect { meals ->
                _favorites.value = meals
            }
        }
    }

    fun addFavorite(meal: MealEntity) {
        viewModelScope.launch {
            repository.addFavorite(meal)
        }
    }

    fun removeFavorite(meal: MealEntity) {
        viewModelScope.launch {
            repository.removeFavorite(meal)
        }
    }
}