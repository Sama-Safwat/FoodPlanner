package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.FavoritesDao
import com.example.foodplanner.data.local.MealEntity
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    fun getFavorites(): Flow<List<MealEntity>> = favoritesDao.getAllFavorites()

    suspend fun addFavorite(meal: MealEntity) = favoritesDao.addFavorite(meal)

    suspend fun removeFavorite(meal: MealEntity) = favoritesDao.removeFavorite(meal)

    suspend fun isFavorite(mealId: String): Boolean =
        favoritesDao.getFavoriteById(mealId) != null
}