package com.example.foodplanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // without suspend
    fun insertMeal(meal: MealEntity)

    @Delete
    fun deleteMeal(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE userId = :userId")
    fun getAllMealsForUser(userId: String): List<MealEntity>

    @Query("SELECT * FROM meals WHERE userId = :userId AND idMeal = :mealId")
    fun getMealById(userId: String, mealId: String): MealEntity?

    @Query("SELECT * FROM meals WHERE userId = :userId AND idMeal = :mealId")
    fun getMealByIdSync(userId: String, mealId: String): MealEntity?

    @Query("DELETE FROM meals WHERE userId = :userId AND idMeal = :mealId")
    fun deleteMealById(userId: String, mealId: String)
}