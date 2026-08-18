package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.FavoritesDao
import com.example.foodplanner.data.local.MealEntity
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(
    private val favoritesDao: FavoritesDao
) {

    fun getFavorites(): Flow<List<MealEntity>> {
        return favoritesDao.getAllFavorites()
    }

    suspend fun addFavorite(meal: MealEntity) {
        favoritesDao.insertFavorite(meal)
    }

    suspend fun removeFavorite(meal: MealEntity) {
        favoritesDao.deleteFavorite(meal)
    }
}