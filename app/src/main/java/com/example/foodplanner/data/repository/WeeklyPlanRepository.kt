package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.PlanDao
import com.example.foodplanner.data.local.PlannedMealEntity
import kotlinx.coroutines.flow.Flow

class WeeklyPlanRepository(private val dao: PlanDao) {

    fun getPlan(): Flow<List<PlannedMealEntity>> = dao.getPlan()

    suspend fun addMealToPlan(meal: PlannedMealEntity) = dao.addToPlan(meal)

    suspend fun removeMealFromPlan(planId: Int) = dao.removeFromPlan(planId)

    suspend fun clearPlan() = dao.clearPlan()
}