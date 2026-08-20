package com.example.foodplanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(meal: MealEntity)

    @Delete
    suspend fun removeFavorite(meal: MealEntity)

    @Query("DELETE FROM meals WHERE userId = :userId AND idMeal = :mealId")
    suspend fun removeFavoriteById(userId: String, mealId: String)

    @Query("SELECT * FROM meals WHERE userId = :userId")
    fun getAllFavorites(userId: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE userId = :userId AND idMeal = :mealId")
    suspend fun getFavoriteById(userId: String, mealId: String): MealEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM meals WHERE userId = :userId AND idMeal = :mealId)")
    suspend fun isFavorite(userId: String, mealId: String): Boolean
}