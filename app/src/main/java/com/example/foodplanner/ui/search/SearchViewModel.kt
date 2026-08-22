package com.example.foodplanner.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MealRemoteRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Meal>>()
    val searchResults: LiveData<List<Meal>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _navigateToMealDetails = MutableLiveData<String?>()
    val navigateToMealDetails: LiveData<String?> = _navigateToMealDetails

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients: LiveData<List<String>> = _ingredients

    init {
        loadCategories()
        loadIngredients()
    }

    fun searchByName(query: String) {
        if (query.isEmpty()) {
            clearResults()
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val call = if (query.length == 1) {
                    repository.searchMealsByFirstLetter(query)
                } else {
                    repository.searchMealsByName(query)
                }
                val meals = call.meals ?: emptyList()
                _isLoading.value = false
                if (meals.isNotEmpty()) {
                    _searchResults.value = meals
                } else {
                    _error.value = "No meals found"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Search failed"
            }
        }
    }

    fun searchByCategory(category: String) {
        if (category.isEmpty()) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMealsByCategory(category)
                val meals = response.meals ?: emptyList()
                _isLoading.value = false
                if (meals.isNotEmpty()) {
                    _searchResults.value = meals
                } else {
                    _error.value = "No meals in this category"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Failed to load meals"
            }
        }
    }

    fun searchByIngredient(ingredient: String) {
        if (ingredient.isEmpty()) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMealsByIngredient(ingredient)
                val meals = response.meals ?: emptyList()
                _isLoading.value = false
                if (meals.isNotEmpty()) {
                    _searchResults.value = meals
                } else {
                    _error.value = "No meals with this ingredient"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Failed to load meals"
            }
        }
    }

    fun searchByCountry(country: String) {
        if (country.isEmpty()) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMealsByArea(country)
                val meals = response.meals ?: emptyList()
                _isLoading.value = false
                if (meals.isNotEmpty()) {
                    _searchResults.value = meals
                } else {
                    _error.value = "No meals found in this country"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Failed to load meals"
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = repository.getCategories()
                val categories = response.categories?.mapNotNull { it.strCategory } ?: emptyList()
                _categories.value = categories
            } catch (e: Exception) {
            }
        }
    }

    private fun loadIngredients() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getIngredients()
                val ingredients = response.meals?.mapNotNull { it.strIngredient } ?: emptyList()
                _isLoading.value = false
                if (ingredients.isNotEmpty()) {
                    _ingredients.value = ingredients
                } else {
                    _error.value = "No ingredients found from API"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Could not load ingredients. Please check your internet connection."
            }
        }
    }

    fun onMealClicked(mealId: String) {
        _navigateToMealDetails.value = mealId
    }

    fun onIngredientClicked(ingredient: String) {
        searchByIngredient(ingredient)
    }

    fun clearResults() {
        _searchResults.value = emptyList()
    }

    fun onNavigationDone() {
        _navigateToMealDetails.value = null
    }

    fun onErrorShown() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}