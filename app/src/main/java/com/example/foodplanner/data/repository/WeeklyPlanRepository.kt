package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.PlanDao
import com.example.foodplanner.data.local.PlannedMealEntity
import com.example.foodplanner.data.sync.SyncManager
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.flow.Flow
import com.example.foodplanner.App

class WeeklyPlanRepository(
    private val dao: PlanDao,
    private val sync: SyncManager? = App.instance.syncManager
) {

    private fun userId(): String = UserProvider.getCurrentUserId()

    fun getPlan(): Flow<List<PlannedMealEntity>> = dao.getPlan(userId())

    fun getMealsForDate(date: String): Flow<List<PlannedMealEntity>> =
        dao.getMealsForDate(userId(), date)

    suspend fun addMealToPlan(meal: PlannedMealEntity) {
        val withUser = meal.copy(userId = userId())
        dao.addToPlan(withUser)
        sync?.backupPlanMeal(withUser)
    }

    suspend fun removeMealFromPlan(planId: Int) {
        val entity = dao.getPlanById(planId)
        dao.removeFromPlan(userId(), planId)
        entity?.let { sync?.removePlanMealBackup(it.date, it.mealId) }
    }

    suspend fun clearDate(date: String) = dao.clearDate(userId(), date)

    suspend fun clearPlan() = dao.clearPlan(userId())
}