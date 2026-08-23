package com.example.foodplanner.ui.planner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.data.local.PlannedMealEntity
import com.example.foodplanner.data.repository.WeeklyPlanRepository
import com.example.foodplanner.utils.DateUtils
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class PlannerViewModel(
    private val repository: WeeklyPlanRepository
) : ViewModel() {

    private val _meals = MutableLiveData<List<PlannedMealEntity>>()
    val meals: LiveData<List<PlannedMealEntity>> = _meals

    private val _selectedDate = MutableLiveData(DateUtils.toIso(Date()))
    val selectedDate: LiveData<String> = _selectedDate

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private var allMeals: List<PlannedMealEntity> = emptyList()

    init {
        observePlan()
    }

    private fun observePlan() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlan().collect { meals ->
                allMeals = meals
                withContext(Dispatchers.Main) {
                    showSelectedDayMeals()
                }
            }
        }
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        showSelectedDayMeals()
    }

    private fun showSelectedDayMeals() {
        val date = _selectedDate.value ?: return
        val filtered = allMeals.filter { it.date == date }
        _meals.value = filtered
    }

    fun addMealToPlan(mealId: String, mealName: String, mealImageUrl: String?) {
        val date = _selectedDate.value ?: return
        val userId = UserProvider.getCurrentUserId()
        val meal = PlannedMealEntity(
            userId = userId,
            date = date,
            mealId = mealId,
            mealName = mealName,
            mealImageUrl = mealImageUrl ?: "https://www.themealdb.com/images/media/meals/placeholder.jpg"
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addMealToPlan(meal)
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "'$mealName' added to ${DateUtils.toDisplay(date)}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "Failed to add meal: ${e.message}"
                }
            }
        }
    }

    fun removeMealFromPlan(meal: PlannedMealEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeMealFromPlan(meal.planId)
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "Meal removed from plan"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "Failed to remove meal: ${e.message}"
                }
            }
        }
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}