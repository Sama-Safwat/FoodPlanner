package com.example.foodplanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Query("SELECT * FROM weekly_plan WHERE userId = :userId ORDER BY date")
    fun getPlan(userId: String): Flow<List<PlannedMealEntity>>

    @Query("SELECT * FROM weekly_plan WHERE userId = :userId AND date = :date")
    fun getMealsForDate(userId: String, date: String): Flow<List<PlannedMealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlan(meal: PlannedMealEntity)

    @Query("DELETE FROM weekly_plan WHERE userId = :userId AND planId = :planId")
    suspend fun removeFromPlan(userId: String, planId: Int)

    @Query("DELETE FROM weekly_plan WHERE userId = :userId AND date = :date")
    suspend fun clearDate(userId: String, date: String)

    @Query("DELETE FROM weekly_plan WHERE userId = :userId")
    suspend fun clearPlan(userId: String)
}