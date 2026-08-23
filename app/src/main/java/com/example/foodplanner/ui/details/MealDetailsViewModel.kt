package com.example.foodplanner.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.FavoritesRepository
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.launch

class MealDetailsViewModel(
    private val remoteRepository: MealRemoteRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _meal = MutableLiveData<Meal?>()
    val meal: LiveData<Meal?> = _meal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _ingredients = MutableLiveData<List<Pair<String, String>>>()
    val ingredients: LiveData<List<Pair<String, String>>> = _ingredients

    private val _videoUrl = MutableLiveData<String?>()
    val videoUrl: LiveData<String?> = _videoUrl

    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean> = _navigateBack as LiveData<Boolean>

    private var currentMeal: Meal? = null

    fun loadMealDetails(mealId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = remoteRepository.getMealDetails(mealId)
                val meal = response.meals?.firstOrNull()
                if (meal != null) {
                    currentMeal = meal
                    _meal.value = meal
                    _ingredients.value = extractIngredients(meal)
                    checkFavoriteStatus(mealId)
                    meal.strYoutube?.let { videoUrl ->
                        if (videoUrl.isNotEmpty()) {
                            _videoUrl.value = videoUrl
                        }
                    }
                } else {
                    _error.value = "Meal not found"
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load meal details"
                _isLoading.value = false
            }
        }
    }

    private suspend fun checkFavoriteStatus(mealId: String) {
        try {
            val userId = UserProvider.getCurrentUserId()
            val isFav = favoritesRepository.isFavorite(mealId)
            _isFavorite.value = isFav
        } catch (e: Exception) {
            _isFavorite.value = false
        }
    }

    fun toggleFavorite() {
        val meal = currentMeal ?: return
        val currentFav = _isFavorite.value ?: false
        val newFav = !currentFav
        _isFavorite.value = newFav

        viewModelScope.launch {
            try {
                val userId = UserProvider.getCurrentUserId()
                if (newFav) {
                    val mealEntity = MealEntity(
                        userId = userId,
                        idMeal = meal.idMeal ?: "",
                        strMeal = meal.strMeal ?: "",
                        strCategory = meal.strCategory,
                        strArea = meal.strArea,
                        strInstructions = meal.strInstructions,
                        strMealThumb = meal.strMealThumb,
                        strYoutube = meal.strYoutube
                    )
                    favoritesRepository.addFavorite(mealEntity)
                } else {
                    meal.idMeal?.let { id ->
                        favoritesRepository.removeFavoriteById(id)
                    }
                }
            } catch (e: Exception) {
                _isFavorite.value = currentFav
                _error.value = "Failed to update favorites"
            }
        }
    }

    fun onBackPressed() {
        _navigateBack.value = true
    }

    fun onNavigationDone() {
        _navigateBack.value = null
    }

    fun onErrorShown() {
        _error.value = null
    }

    private fun extractIngredients(meal: Meal): List<Pair<String, String>> {
        val ingredients = mutableListOf<Pair<String, String>>()

        val ingredientFields = listOf(
            meal.strIngredient1, meal.strIngredient2, meal.strIngredient3,
            meal.strIngredient4, meal.strIngredient5, meal.strIngredient6,
            meal.strIngredient7, meal.strIngredient8, meal.strIngredient9,
            meal.strIngredient10, meal.strIngredient11, meal.strIngredient12,
            meal.strIngredient13, meal.strIngredient14, meal.strIngredient15,
            meal.strIngredient16, meal.strIngredient17, meal.strIngredient18,
            meal.strIngredient19, meal.strIngredient20
        )

        val measureFields = listOf(
            meal.strMeasure1, meal.strMeasure2, meal.strMeasure3,
            meal.strMeasure4, meal.strMeasure5, meal.strMeasure6,
            meal.strMeasure7, meal.strMeasure8, meal.strMeasure9,
            meal.strMeasure10, meal.strMeasure11, meal.strMeasure12,
            meal.strMeasure13, meal.strMeasure14, meal.strMeasure15,
            meal.strMeasure16, meal.strMeasure17, meal.strMeasure18,
            meal.strMeasure19, meal.strMeasure20
        )

        for (i in ingredientFields.indices) {
            val ingredient = ingredientFields[i]
            val measure = measureFields.getOrNull(i)

            if (!ingredient.isNullOrEmpty() && !ingredient.isBlank()) {
                val measureText = if (!measure.isNullOrEmpty() && !measure.isBlank()) {
                    measure
                } else {
                    ""
                }
                ingredients.add(Pair(ingredient, measureText))
            }
        }

        return ingredients
    }

    override fun onCleared() {
        super.onCleared()
    }
}