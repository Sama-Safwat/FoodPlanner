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
    suspend fun insertFavorite(meal: MealEntity)

    @Delete
    suspend fun deleteFavorite(meal: MealEntity)

    @Query("SELECT * FROM meals")
    fun getAllFavorites(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE idMeal = :mealId")
    suspend fun getFavoriteById(mealId: String): MealEntity?
}