package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.PlanDao
import com.example.foodplanner.data.local.PlannedMealEntity
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.flow.Flow

class WeeklyPlanRepository(
    private val dao: PlanDao
) {

    private fun userId(): String = UserProvider.getCurrentUserId()

    fun getPlan(): Flow<List<PlannedMealEntity>> {
        return dao.getPlan(userId())
    }

    fun getMealsForDate(date: String): Flow<List<PlannedMealEntity>> {
        return dao.getMealsForDate(userId(), date)
    }

    suspend fun addMealToPlan(meal: PlannedMealEntity) {
        dao.addToPlan(meal.copy(userId = userId()))
    }

    suspend fun removeMealFromPlan(planId: Int) {
        dao.removeFromPlan(userId(), planId)
    }

    suspend fun clearDate(date: String) {
        dao.clearDate(userId(), date)
    }

    suspend fun clearPlan() {
        dao.clearPlan(userId())
    }
}