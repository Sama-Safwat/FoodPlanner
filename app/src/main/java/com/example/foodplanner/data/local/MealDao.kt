package com.example.foodplanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: MealEntity)

    @Delete
    fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE idMeal = :mealId")
    fun deleteMealById(mealId: String)

    @Query("SELECT * FROM meals")
    fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM meals WHERE idMeal = :mealId")
    fun getMealById(mealId: String): MealEntity?
}