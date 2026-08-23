package com.example.foodplanner.ui.countries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.model.Area
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

private object CountriesCache {
    var areasWithMeals: List<Area>? = null
}
class CountriesViewModel(
    private val repository: MealRemoteRepository
) : ViewModel() {

    private val _countries = MutableLiveData<List<Area>>()
    val countries: LiveData<List<Area>> = _countries

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val _navigateToMealDetails = MutableLiveData<String?>()
    val navigateToMealDetails: LiveData<String?> = _navigateToMealDetails

    init {
        loadCountries()
    }

    fun loadCountries(forceRefresh: Boolean = false) {
        val cached = CountriesCache.areasWithMeals
        if (!forceRefresh && cached != null) {
            _countries.value = cached
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allAreas = repository.getAreas().meals ?: emptyList()

                val areasWithMeals = mutableListOf<Area>()
                for (area in allAreas) {
                    if (areaHasMeals(area.strArea)) {
                        areasWithMeals.add(area)
                    }
                    delay(200)
                }

                CountriesCache.areasWithMeals = areasWithMeals
                _countries.value = areasWithMeals
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private suspend fun areaHasMeals(areaName: String?): Boolean {
        if (areaName.isNullOrBlank()) return false
        var attempt = 0
        while (attempt < 3) {
            try {
                return repository.getMealsByArea(areaName).meals?.isNotEmpty() == true
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    delay(600L * (attempt + 1))
                } else {
                    return false
                }
            } catch (e: Exception) {
                delay(300)
            }
            attempt++
        }
        return false
    }

    fun loadMealsByCountry(country: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val mealsList = fetchMealsByAreaWithRetry(country)
                if (mealsList.isNotEmpty()) {
                    _meals.value = mealsList
                } else {
                    _toastMessage.value = "No meals found in this country"
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _toastMessage.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    private suspend fun fetchMealsByAreaWithRetry(country: String): List<Meal> {
        var attempt = 0
        while (attempt < 3) {
            try {
                return repository.getMealsByArea(country).meals ?: emptyList()
            } catch (e: HttpException) {
                if (e.code() == 429 && attempt < 2) {
                    delay(600L * (attempt + 1))
                    attempt++
                } else {
                    throw e
                }
            }
        }
        return emptyList()
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