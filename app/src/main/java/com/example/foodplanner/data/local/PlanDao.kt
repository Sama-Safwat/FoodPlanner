package com.example.foodplanner.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM weekly_plan ORDER BY planId")
    fun getPlan(): Flow<List<PlannedMealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlan(meal: PlannedMealEntity)

    @Query("SELECT * FROM weekly_plan WHERE date = :date")
    fun getMealsForDate(date: String): Flow<List<PlannedMealEntity>>

    @Query("DELETE FROM weekly_plan WHERE planId = :planId")
    suspend fun removeFromPlan(planId: Int)

    @Query("DELETE FROM weekly_plan")
    suspend fun clearPlan()
}