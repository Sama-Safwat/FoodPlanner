package com.example.foodplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MealRemoteRepository
) : ViewModel() {

    private val _mealOfTheDay = MutableLiveData<Meal?>()
    val mealOfTheDay: LiveData<Meal?> = _mealOfTheDay

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadMealOfTheDay()
        loadMeals()
    }

    fun loadMealOfTheDay() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getRandomMeal()
                _mealOfTheDay.value = response.meals?.firstOrNull()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Unable to load meal of the day"
                _isLoading.value = false
            }
        }
    }

    fun loadMeals() {
        viewModelScope.launch {
            try {
                val response = repository.searchMealsByName("a")
                val mealsList = response.meals.orEmpty()
                if (mealsList.isNotEmpty()) {
                    _meals.value = mealsList.take(10)
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}