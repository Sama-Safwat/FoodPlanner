package com.example.foodplanner.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.model.Category
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val repository: MealRemoteRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private var _navigateToMealDetails = MutableLiveData<String?>()
    val navigateToMealDetails: LiveData<String?> = _navigateToMealDetails

    init {
        loadCategories()
    }

    fun loadCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getCategories()
                _categories.value = response.categories ?: emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun loadMealsByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMealsByCategory(category)
                val mealsList = response.meals ?: emptyList()
                if (mealsList.isNotEmpty()) {
                    _meals.value = mealsList
                } else {
                    _toastMessage.value = "No meals found in this category"
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun onMealClicked(mealId: String) {
        _navigateToMealDetails.value = mealId
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun onNavigationDone() {
        _navigateToMealDetails.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}